package com.agileboot.domain.biz.customer.command;

import lombok.Data;

@Data
public class AppLoginCommand {
    private String phoneCode;
    private String loginCode;
}
