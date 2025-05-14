package com.agileboot.domain.biz.statistics.dto;

import com.agileboot.common.annotation.ExcelColumn;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class StatisticsByTimeDTO {
    @ExcelColumn(name = "门店名称")
    private String deptName;

    @ExcelColumn(name = "操作员工")
    private String operator;

    @ExcelColumn(name = "顾客姓名")
    private String customerName;

    @ExcelColumn(name = "顾客手机")
    private String customerPhone;

    @ExcelColumn(name = "类别", mapping = "1:充值;2:核销;")
    private Integer type;

    @ExcelColumn(name = "产品")
    private String product;

    @ExcelColumn(name = "费用")
    private Integer amount;

    @ExcelColumn(name = "流水号")
    private String serialNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发生时间")
    @ExcelColumn(name = "时间")
    private Date createTime;

    private String attachment;
}
