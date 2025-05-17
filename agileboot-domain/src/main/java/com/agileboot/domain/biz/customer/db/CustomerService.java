package com.agileboot.domain.biz.customer.db;

import com.agileboot.common.core.page.PageDTO;
import com.agileboot.domain.biz.customer.command.CustomerQuery;
import com.agileboot.domain.biz.customer.dto.CustomerBalanceDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:43:33
 */
public interface CustomerService extends IService<Customer> {
    /**
     * 创建客户
     *
     * @param customer 客户信息
     * @return 是否成功
     */
    boolean createCustomerBalance(Customer customer);


    Customer getByPhone(String phone);

    PageDTO<CustomerBalanceDTO> listCustomerBalance(CustomerQuery command);

    Customer findByOpenId(String openId);
}
