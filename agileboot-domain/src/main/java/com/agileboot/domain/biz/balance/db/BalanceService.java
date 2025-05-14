package com.agileboot.domain.biz.balance.db;

import com.agileboot.domain.biz.balance.command.ConsumeCommand;
import com.agileboot.domain.biz.balance.command.RechargeCommand;
import com.agileboot.domain.biz.balance.dto.CustomerLog;
import com.agileboot.domain.biz.customer.db.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:44:06
 */
public interface BalanceService extends IService<Balance> {

    // 创建账户
    Balance createAccount(Long deptId, Customer customer);
    /**
     * 充值
     *
     * @param customerId 客户ID
     * @param amount     充值金额
     * @return 是否成功
     */
    boolean recharge(RechargeCommand command);

    /**
     * 消费
     *
     * @param customerId 客户ID
     * @param amount     消费金额
     * @return 是否成功
     */
    boolean consume(ConsumeCommand command);


    List<CustomerLog> log(String customerId);

    List<Balance> getByCustomerId(Long customerId);
}
