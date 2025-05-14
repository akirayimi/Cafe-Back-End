package com.agileboot.domain.biz.customer.db;

import com.agileboot.domain.biz.customer.command.CustomerQuery;
import com.agileboot.domain.biz.customer.dto.CustomerBalanceDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:43:33
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    Page<CustomerBalanceDTO> listCustomer(Page<Customer> page, @Param("param") CustomerQuery command);
}
