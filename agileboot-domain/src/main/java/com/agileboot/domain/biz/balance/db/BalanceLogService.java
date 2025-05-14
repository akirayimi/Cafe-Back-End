package com.agileboot.domain.biz.balance.db;

import com.agileboot.domain.biz.customer.db.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:44:16
 */
public interface BalanceLogService extends IService<BalanceLog> {
    BalanceLog createLog(Balance balance, BalanceLogType type, Integer amount);
}
