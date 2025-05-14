package com.agileboot.domain.biz.statistics.db;

import com.agileboot.common.config.BizConfig;
import com.agileboot.common.core.page.PageDTO;
import com.agileboot.common.utils.poi.CustomExcelUtil;
import com.agileboot.common.utils.time.DatePickUtil;
import com.agileboot.domain.biz.statistics.command.StatisticsByTimeQuery;
import com.agileboot.domain.biz.statistics.dto.StatisticsByTimeDTO;
import com.agileboot.infrastructure.user.AuthenticationUtils;
import com.agileboot.infrastructure.user.web.SystemLoginUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{
    final private StatisticsMapper statisticsMapper;
    final private BizConfig bizConfig;

    @Override
    public PageDTO<StatisticsByTimeDTO> getStatisticsByTime(StatisticsByTimeQuery command) {
        if (command.getBeginTime() != null) {
            command.setBeginTime(DatePickUtil.getBeginOfTheDay(command.getBeginTime()));
        }
        if (command.getEndTime() != null) {
            command.setEndTime(DatePickUtil.getEndOfTheDay(command.getEndTime()));
        }

        fillDataRange(command);

        IPage<StatisticsByTimeDTO> result = statisticsMapper.getStatisticsByTime(command.toPage(), command);
        return new PageDTO<>(result.getRecords(), result.getTotal());
    }

    private void fillDataRange(StatisticsByTimeQuery command) {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        if (bizConfig.getFullDataAccessRoleKey().contains(loginUser.getRoleInfo().getRoleKey())) {
            return;
        } else {
            command.setDeptId(loginUser.getDeptId());
        }
    }

    @Override
    public void exportFinancial(ServletOutputStream outputStream, StatisticsByTimeQuery param) {
        PageDTO<StatisticsByTimeDTO> result = getStatisticsByTime(param);

        result.getRows().forEach(r -> r.setAmount(Math.abs(r.getAmount())));
        CustomExcelUtil.writeToOutputStream(result.getRows(), StatisticsByTimeDTO.class, outputStream);
    }
}
