package com.agileboot.api.customize.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.agileboot.api.customize.config.JwtAuthenticationFilter;
import com.agileboot.common.constant.Constants;
import com.agileboot.common.enums.common.LoginStatusEnum;
import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode;
import com.agileboot.common.utils.i18n.MessageUtils;
import com.agileboot.common.utils.jackson.JacksonUtil;
import com.agileboot.domain.biz.customer.command.AppLoginCommand;
import com.agileboot.domain.biz.customer.db.Customer;
import com.agileboot.domain.biz.customer.db.CustomerService;
import com.agileboot.domain.common.cache.RedisCacheService;
import com.agileboot.infrastructure.thread.ThreadPoolManager;
import com.agileboot.infrastructure.user.app.AppLoginUser;
import com.agileboot.infrastructure.user.web.SystemLoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.agileboot.api.exception.AppExceptionCode.WX_LOGIN_CODE_TO_SESSION_ERROR;
import static com.agileboot.api.exception.AppExceptionCode.WX_LOGIN_NOT_BIND;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AppLoginService {
    private final WxMaService wxMaService;

    private final CustomerService customerService;

    private final AuthenticationManager authenticationManager;

    private final RedisCacheService redisCache;

    private final JwtTokenService jwtTokenService;

    public String loginWithCode(String code) {
//        WxMaJscode2SessionResult sessionInfo;
//        try {
//            sessionInfo = wxMaService.getUserService().getSessionInfo(code);
//        } catch (WxErrorException e) {
//            throw new ApiException(WX_LOGIN_CODE_TO_SESSION_ERROR, e.getError().getErrorCode(), e.getError().getErrorMsg());
//        }
//
//        String openId = sessionInfo.getOpenid();
//        Customer customer = customerService.findByOpenId(openId);
        Customer customer = customerService.getByPhone("15828664816");
        if (customer == null) {
            throw new ApiException(WX_LOGIN_NOT_BIND);
        }

        return postLogin(customer);
    }

    public String loginWithPhone(AppLoginCommand command) {
        log.info("command: {}", JacksonUtil.to(command));
        WxMaPhoneNumberInfo phoneNumber;
        WxMaJscode2SessionResult sessionInfo;
        try {
            sessionInfo = wxMaService.getUserService().getSessionInfo(command.getLoginCode());
            log.info("sessionInfo: {}", JacksonUtil.to(sessionInfo));
            phoneNumber = wxMaService.getUserService().getPhoneNumber(command.getPhoneCode());
            log.info("phoneNumber: {}", JacksonUtil.to(phoneNumber));
        } catch (WxErrorException e) {
            throw new ApiException(WX_LOGIN_CODE_TO_SESSION_ERROR, e.getError().getErrorCode(), e.getError().getErrorMsg());
        }
        String phone = phoneNumber.getPhoneNumber();
        Customer customer = customerService.getByPhone(phone);
        if (customer == null) {
            customer = new Customer();
            customer.setName(phone);
            customer.setPhone(phone);
            customer.setCreatorId(-1L);
            customer.setOpenId(sessionInfo.getOpenid());
            customer.setUnionId(sessionInfo.getUnionid());
            customerService.save(customer);
        } else {
            customer.setOpenId(sessionInfo.getOpenid());
            customer.setUnionId(sessionInfo.getUnionid());
            customerService.updateById(customer);
        }

        return postLogin(customer);
    }

    private String postLogin(Customer customer){
        Authentication authentication;
        try {
            // 该方法会去调用UserDetailsServiceImpl#loadUserByUsername  校验用户名和密码  认证鉴权
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(new AppLoginUser(customer.getId()), customer.getId()));
        } catch (BadCredentialsException e) {
            throw new ApiException(e, ErrorCode.Business.LOGIN_WRONG_USER_PASSWORD);
        } catch (Exception e) {
            throw new ApiException(e, ErrorCode.Business.LOGIN_ERROR, e.getMessage());
        }

        // 把当前登录用户 放入上下文中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成token
        return jwtTokenService.generateToken(MapUtil.of(Constants.Token.LOGIN_APP_USER_ID, customer.getId()));
    }
}
