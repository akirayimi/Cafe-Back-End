package com.agileboot.domain.biz.balance.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:44:16
 */
@Getter
@Setter
@TableName("biz_balance_log")
@ApiModel(value = "BalanceLog对象", description = "")
public class BalanceLog extends Model<BalanceLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("customer_id")
    private Long customerId;

    @TableField("customer_name")
    private String customerName;

    @ApiModelProperty("1-充值，2-消费")
    @TableField("`type`")
    private Integer type;

    @ApiModelProperty("额度")
    @TableField("amount")
    private Integer amount;

    @TableField("operate_user_id")
    private Long operateUserId;

    @TableField("operate_user_name")
    private String operateUserName;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
