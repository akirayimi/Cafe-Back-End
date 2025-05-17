package com.agileboot.api.customize.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.agileboot.common.enums.BasicEnumUtil;
import com.agileboot.common.enums.common.UserStatusEnum;
import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode;
import com.agileboot.domain.biz.customer.db.Customer;
import com.agileboot.domain.biz.customer.db.CustomerService;
import com.agileboot.domain.system.menu.db.SysMenuEntity;
import com.agileboot.domain.system.menu.db.SysMenuService;
import com.agileboot.domain.system.role.db.SysRoleEntity;
import com.agileboot.domain.system.role.db.SysRoleService;
import com.agileboot.domain.system.user.db.SysUserEntity;
import com.agileboot.domain.system.user.db.SysUserService;
import com.agileboot.infrastructure.user.app.AppLoginUser;
import com.agileboot.infrastructure.user.web.DataScopeEnum;
import com.agileboot.infrastructure.user.web.RoleInfo;
import com.agileboot.infrastructure.user.web.SystemLoginUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 自定义加载用户信息通过用户名
 * 用于SpringSecurity 登录流程
 * 没有办法把这个类 放进loginService中  会在SecurityConfig中造成循环依赖
 * @author valarchie
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppLoginUser(Long.valueOf(username));
    }

}
