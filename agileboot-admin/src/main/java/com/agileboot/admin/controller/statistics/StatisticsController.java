package com.agileboot.admin.controller.statistics;

import cn.hutool.core.date.DateUtil;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.domain.biz.statistics.command.StatisticsByTimeQuery;
import com.agileboot.domain.biz.statistics.db.StatisticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Tag(name = "统计API", description = "报表接口")
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/byTime")
    @PreAuthorize("@permission.has('statistics:financial:list')")
    public ResponseDTO byTime(StatisticsByTimeQuery param) {
        return ResponseDTO.ok(statisticsService.getStatisticsByTime(param));
    }

    @GetMapping("/financial/export")
    @PreAuthorize("@permission.has('statistics:financial:list')")
    public void byTime(StatisticsByTimeQuery param, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        String fileName = "门店流水" + DateUtil.format(new Date(), NORM_DATETIME_PATTERN) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)));
        statisticsService.exportFinancial(response.getOutputStream(), param);
    }
}
