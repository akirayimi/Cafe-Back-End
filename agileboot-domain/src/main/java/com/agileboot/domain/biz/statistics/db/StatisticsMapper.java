package com.agileboot.domain.biz.statistics.db;

import com.agileboot.domain.biz.statistics.command.StatisticsByTimeQuery;
import com.agileboot.domain.biz.statistics.dto.StatisticsByTimeDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface StatisticsMapper extends BaseMapper {
    IPage<StatisticsByTimeDTO> getStatisticsByTime(Page<StatisticsByTimeDTO> page, @Param("param") StatisticsByTimeQuery param);
}
