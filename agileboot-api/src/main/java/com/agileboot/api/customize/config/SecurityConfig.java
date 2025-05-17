package com.agileboot.api.customize.config;

import com.agileboot.api.customize.service.UserDetailsServiceImpl;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.common.exception.ApiException;
import com.agileboot.common.exception.error.ErrorCode.Client;
import com.agileboot.common.utils.ServletHolderUtil;
import com.agileboot.common.utils.jackson.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * 主要配置登录流程逻辑涉及以下几个类

 * @author valarchie
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * token认证过滤器
     */
    private final JwtAuthenticationFilter jwtTokenFilter;

    private final UserDetailsServiceImpl userDetailsService;


    /**
     * 跨域过滤器
     */
    private final CorsFilter corsFilter;


    /**
     * 登录异常处理类
     * 用户未登陆的话  在这个Bean中处理
     */
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, exception) -> {
            ResponseDTO<Void> responseDTO = ResponseDTO.fail(
                new ApiException(Client.COMMON_NO_AUTHORIZATION, request.getRequestURI())
            );
            ServletHolderUtil.renderString(response, JacksonUtil.to(responseDTO));
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
            // 不配这个错误处理的话 会直接返回403
            .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint())
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 禁用 session
            .and()
            .authorizeRequests()
            .antMatchers("/swagger-ui.html").anonymous()
            .antMatchers("/swagger-resources/**").anonymous()
            .antMatchers("/webjars/**").anonymous()
            .antMatchers("/*/api-docs","/*/api-docs/swagger-config").anonymous()
            .antMatchers("/**/api-docs.yaml" ).anonymous()
            .antMatchers("/common/**").permitAll()
            .anyRequest().authenticated()
            .and()
            // 禁用 X-Frame-Options 响应头。下面是具体解释：
            // X-Frame-Options 是一个 HTTP 响应头，用于防止网页被嵌入到其他网页的 <frame>、<iframe> 或 <object> 标签中，从而可以减少点击劫持攻击的风险
            .headers().frameOptions().disable()
            .and()
            .formLogin().disable();

        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationFilter.class);


        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return "";
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        };
    }
}
