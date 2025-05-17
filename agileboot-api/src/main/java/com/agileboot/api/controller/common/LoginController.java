package com.agileboot.api.controller.common;

import cn.hutool.core.map.MapUtil;
import com.agileboot.api.customize.service.AppLoginService;
import com.agileboot.api.customize.service.JwtTokenService;
import com.agileboot.common.core.base.BaseController;
import com.agileboot.common.core.dto.ResponseDTO;

import java.util.Map;

import com.agileboot.domain.biz.customer.command.AppLoginCommand;
import com.agileboot.domain.biz.customer.db.Customer;
import com.agileboot.domain.biz.customer.db.CustomerService;
import com.agileboot.domain.biz.customer.dto.AppUserDTO;
import com.agileboot.infrastructure.user.AuthenticationUtils;
import com.agileboot.infrastructure.user.app.AppLoginUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调度日志操作处理
 *
 * @author ruoyi
 */
@RestController
@Tag(name = "APP-登录API", description = "APP登录相关接口")
@RequestMapping("/common")
@AllArgsConstructor
public class LoginController extends BaseController {
    private final AppLoginService appLoginService;
    private final CustomerService customerService;

    @PostMapping("/test")
    public ResponseDTO<String> test() {
        return ResponseDTO.fail("登录成功");
    }

    @PostMapping("/app/loginWithCode")
    public ResponseDTO<AppUserDTO> loginWithCode(@RequestBody AppLoginCommand appLoginCommand) {
        String token = appLoginService.loginWithCode(appLoginCommand.getLoginCode());
        return ResponseDTO.ok(buildAppUserDTO(token));
    }

    @PostMapping("/app/loginWithPhone")
    public ResponseDTO<AppUserDTO> loginWithPhone(@RequestBody AppLoginCommand appLoginCommand) {
        String token = appLoginService.loginWithPhone(appLoginCommand);
        return ResponseDTO.ok(buildAppUserDTO(token));
    }

    private AppUserDTO buildAppUserDTO(String token) {
        Long customerId = AuthenticationUtils.getAppUserId();
        Customer customer = customerService.getById(customerId);
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setNickname(customer.getName());
        appUserDTO.setToken(token);
        return appUserDTO;
    }
}
