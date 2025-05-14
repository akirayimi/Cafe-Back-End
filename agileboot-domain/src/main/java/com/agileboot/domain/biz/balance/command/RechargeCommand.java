package com.agileboot.domain.biz.balance.command;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * 充值
 */
@Data
public class RechargeCommand {
    private Long customerId;
    private Integer amount;

    private String serialNumber;
    private String attachment;
}
