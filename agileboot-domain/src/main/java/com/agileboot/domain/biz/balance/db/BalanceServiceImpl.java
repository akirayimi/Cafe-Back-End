package com.agileboot.domain.biz.balance.db;

import com.agileboot.common.exception.ApiException;
import com.agileboot.domain.biz.balance.command.ConsumeCommand;
import com.agileboot.domain.biz.balance.command.RechargeCommand;
import com.agileboot.domain.biz.balance.dto.CustomerLog;
import com.agileboot.domain.biz.customer.db.Customer;
import com.agileboot.domain.biz.customer.db.CustomerMapper;
import com.agileboot.infrastructure.user.AuthenticationUtils;
import com.agileboot.infrastructure.user.web.SystemLoginUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.agileboot.common.exception.error.ErrorCode.Business.*;
import static com.agileboot.common.exception.error.ErrorCode.Internal.INVALID_PARAMETER;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:44:06
 */
@Service
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements BalanceService {
    @Autowired
    private BalanceLogService logService;
    @Autowired
    private BalanceLogAttService attService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private BalanceLogMapper balanceLogMapper;

    @Override
    public Balance createAccount(Long deptId, Customer customer) {
        if (customer.getId() == null ) {
            throw new ApiException(INVALID_PARAMETER, "需要指定客户");
        }
        Balance exist = getBalance(deptId, customer.getId());
        if (exist != null) {
            throw new ApiException(COMMON_RECORD_EXIST, "客户账户");
        }

        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        Balance balanceEntity = new Balance();
        balanceEntity.setCustomerId(customer.getId());
        balanceEntity.setBalance(0);
        balanceEntity.setUpdateTime(new Date());
        balanceEntity.setPhone(customer.getPhone());
        balanceEntity.setName(customer.getName());
        balanceEntity.setCreatorId(loginUser.getUserId());
        balanceEntity.setUpdaterId(loginUser.getUserId());
        balanceEntity.setDeptId(deptId);

        save(balanceEntity);

        return balanceEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recharge(RechargeCommand command) {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        command.setAmount(Math.abs(command.getAmount()));
        Balance balance = getBalance(loginUser.getDeptId(), command.getCustomerId());
        if (balance == null) {
            return false;
        }
        Customer customer = customerMapper.selectById(command.getCustomerId());
        balance.setBalance(balance.getBalance() + command.getAmount());

        BalanceLog log = logService.createLog(balance, BalanceLogType.RECHARGE, command.getAmount());
        attService.createAtt(log.getId(), convert(command));

        balance.updateById();
        return true;
    }

    private ConsumeCommand convert(RechargeCommand command) {
        ConsumeCommand consumeCommand = new ConsumeCommand();
        consumeCommand.setCustomerId(command.getCustomerId());
        consumeCommand.setAmount(command.getAmount());
        consumeCommand.setSerialNumber(command.getSerialNumber());
        consumeCommand.setAttachment(command.getAttachment());
        return consumeCommand;
    }

    @Override
    public boolean consume(ConsumeCommand command) {
        command.setAmount(-Math.abs(command.getAmount()));
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();

        Balance balance = getBalance(loginUser.getDeptId(), command.getCustomerId());
        if (balance == null) {
            return false;
        }
        if (balance.getBalance() < Math.abs(command.getAmount())) {
            throw new ApiException(BALANCE_NOT_ENOUGH);
        }
        Customer customer = customerMapper.selectById(command.getCustomerId());
        balance.setBalance(balance.getBalance() + command.getAmount());

        BalanceLog log = logService.createLog(balance, BalanceLogType.CONSUME, command.getAmount());
        attService.createAtt(log.getId(), command);

        balance.updateById();
        return true;
    }

    private Balance getBalance(Long deptId, Long customerId) {
        if (customerId == null) {
            return null;
        }

        LambdaQueryWrapper<Balance> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Balance::getCustomerId, customerId);
        queryWrapper.eq(Balance::getDeptId, deptId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<CustomerLog> log(String customerId) {
        if (customerId == null) {
            throw new ApiException(INVALID_PARAMETER, "需要指定客户");
        }

        return balanceLogMapper.log(customerId, 2);
    }

    @Override
    public List<Balance> getByCustomerId(Long customerId) {
        if (customerId == null) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<Balance> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Balance::getCustomerId, customerId);
        return baseMapper.selectList(queryWrapper);
    }
}
