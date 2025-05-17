package com.agileboot.domain.biz.customer.db;

import com.agileboot.common.core.page.PageDTO;
import com.agileboot.common.exception.ApiException;
import com.agileboot.domain.biz.balance.db.Balance;
import com.agileboot.domain.biz.customer.command.CustomerQuery;
import com.agileboot.domain.biz.balance.db.BalanceService;
import com.agileboot.domain.biz.customer.dto.CustomerBalanceDTO;
import com.agileboot.domain.system.log.dto.LoginLogDTO;
import com.agileboot.domain.system.notice.db.SysNoticeEntity;
import com.agileboot.infrastructure.user.AuthenticationUtils;
import com.agileboot.infrastructure.user.web.SystemLoginUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.agileboot.common.exception.error.ErrorCode.Business.COMMON_RECORD_EXIST;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:43:33
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Autowired
    private BalanceService balanceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCustomerBalance(Customer customer) {
        Customer existingCustomer = getByPhone(customer.getPhone());
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();

        if (existingCustomer == null) {
            customer.setCreatorId(loginUser.getUserId());
            customer.setUpdaterId(loginUser.getUserId());
            customer.insert();

            balanceService.createAccount(loginUser.getDeptId(), customer);
        } else {
            existingCustomer.setName(customer.getName());
            balanceService.createAccount(loginUser.getDeptId(), existingCustomer);
        }

        return true;
    }

    @Override
    public Customer getByPhone(String phone) {
        LambdaQueryWrapper<Customer> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Customer::getPhone, phone);
        return getOne(queryWrapper);
    }

    @Override
    public PageDTO<CustomerBalanceDTO> listCustomerBalance(CustomerQuery command) {
        SystemLoginUser user = AuthenticationUtils.getSystemLoginUser();
        command.setDeptId(user.getDeptId());
//        Page<SysNoticeEntity> page = noticeService.getNoticeList(query.toPage(), query.toQueryWrapper());
        Page<CustomerBalanceDTO> page = baseMapper.listCustomer(command.toPage(), command);
        return new PageDTO<>(page.getRecords(), page.getTotal());
    }

    @Override
    public Customer findByOpenId(String openId) {
        LambdaQueryWrapper<Customer> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Customer::getOpenId, openId);
        return baseMapper.selectOne(queryWrapper);
    }
}
