package com.agileboot.domain.biz.statistics.db;

import com.agileboot.common.core.page.PageDTO;
import com.agileboot.domain.biz.statistics.command.StatisticsByTimeQuery;
import com.agileboot.domain.biz.statistics.dto.StatisticsByTimeDTO;

import javax.servlet.ServletOutputStream;

public interface StatisticsService {
    PageDTO<StatisticsByTimeDTO> getStatisticsByTime(StatisticsByTimeQuery command);

    void exportFinancial(ServletOutputStream outputStream, StatisticsByTimeQuery param);
}
