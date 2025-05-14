package com.agileboot.domain.biz.customer.dto;

import com.agileboot.domain.biz.customer.db.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerBalanceDTO extends Customer {
    private Integer balance;
}
