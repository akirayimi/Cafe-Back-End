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
 * @since 2025-04-25 14:44:23
 */
@Getter
@Setter
@TableName("biz_balance_log_att")
@ApiModel(value = "BalanceLogAtt对象", description = "")
public class BalanceLogAtt extends Model<BalanceLogAtt> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("log_id")
    private Long logId;

    @TableField("product")
    private String product;

    @ApiModelProperty("流水号")
    @TableField("serial_num")
    private String serialNum;

    @ApiModelProperty("附件")
    @TableField("attachment")
    private String attachment;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
