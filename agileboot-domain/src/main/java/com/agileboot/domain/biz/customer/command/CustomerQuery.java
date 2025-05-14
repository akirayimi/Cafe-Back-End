package com.agileboot.domain.biz.customer.command;

import com.agileboot.common.core.page.AbstractPageQuery;
import com.agileboot.domain.biz.customer.db.Customer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;

@Data
public class CustomerQuery extends AbstractPageQuery<Customer> {
    private String name;
    private String phoneSuffix;
    private Long deptId;

    @Override
    public QueryWrapper<Customer> addQueryCondition() {
        return new QueryWrapper<Customer>();
    }
}
