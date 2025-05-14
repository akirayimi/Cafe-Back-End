package com.agileboot.domain.biz.balance.db;

import com.agileboot.common.enums.BasicEnum;

public enum BalanceLogType implements BasicEnum<Integer> {

    RECHARGE(1, "充值"),
    CONSUME(2, "消费"),
    REFUND(3, "退款"),
    TRANSFER(4, "转账"),
    ;

    private final int value;
    private final String description;

    BalanceLogType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String description() {
        return description;
    }
}
