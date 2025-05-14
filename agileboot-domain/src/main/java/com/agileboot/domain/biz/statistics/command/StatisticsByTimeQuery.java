package com.agileboot.domain.biz.statistics.command;

import com.agileboot.common.core.page.AbstractPageQuery;
import com.agileboot.domain.biz.statistics.dto.StatisticsByTimeDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatisticsByTimeQuery extends AbstractPageQuery<StatisticsByTimeDTO> {
    @ApiModelProperty(value = "部门ID")
    private Long deptId;
    @ApiModelProperty(value = "是否包含子部门")
    private Boolean includeChildDept;
    @ApiModelProperty(value = "款项类型，1:充值，2：核销")
    private Integer type;

    @Override
    public QueryWrapper<StatisticsByTimeDTO> addQueryCondition() {
        return null;
    }
}
