package com.agileboot.domain.biz.balance.command;

import lombok.Data;

/**
 * 充值
 */
@Data
public class ConsumeCommand {
    private Long customerId;
    private Integer amount;

    private String product;
    private String serialNumber;
    private String attachment;
}
