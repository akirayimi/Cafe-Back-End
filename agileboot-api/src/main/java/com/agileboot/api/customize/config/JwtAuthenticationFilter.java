package com.agileboot.api.customize.config;

import com.agileboot.api.customize.service.JwtTokenService;
import com.agileboot.common.constant.Constants;
import com.agileboot.infrastructure.user.app.AppLoginUser;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * token过滤器 验证token有效性
 * 继承OncePerRequestFilter类的话  可以确保只执行filter一次， 避免执行多次
 * @author valarchie
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenFromRequest = jwtTokenService.getTokenFromRequest(request);

        if (tokenFromRequest != null) {
            Claims claims = jwtTokenService.parseToken(tokenFromRequest);
            Long id = MapUtils.getLong(claims, Constants.Token.LOGIN_APP_USER_ID);
            if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(new AppLoginUser(id), id));
                // 把当前登录用户 放入上下文中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
