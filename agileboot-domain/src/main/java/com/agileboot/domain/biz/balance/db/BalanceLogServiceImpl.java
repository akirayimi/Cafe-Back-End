package com.agileboot.domain.biz.balance.db;

import com.agileboot.domain.biz.customer.db.Customer;
import com.agileboot.infrastructure.user.AuthenticationUtils;
import com.agileboot.infrastructure.user.web.SystemLoginUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:44:16
 */
@Service
public class BalanceLogServiceImpl extends ServiceImpl<BalanceLogMapper, BalanceLog> implements BalanceLogService {

    @Override
    public BalanceLog createLog(Balance balance, BalanceLogType type, Integer amount) {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        BalanceLog log = new BalanceLog();
        log.setCustomerId(balance.getCustomerId());
        log.setCustomerName(balance.getName());
        log.setType(type.getValue());
        log.setAmount(amount);
        log.setOperateUserId(loginUser.getUserId());
        log.setOperateUserName(loginUser.getUsername());

        save(log);
        return log;
    }
}
