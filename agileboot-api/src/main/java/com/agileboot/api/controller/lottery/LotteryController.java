package com.agileboot.api.controller.lottery;

import com.agileboot.api.customize.service.JwtTokenService;
import com.agileboot.common.core.base.BaseController;
import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.infrastructure.user.AuthenticationUtils;
import com.agileboot.infrastructure.user.app.AppLoginUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调度日志操作处理
 *
 * @author ruoyi
 */
@RestController
@Tag(name = "APP-抽奖API", description = "APP抽奖相关接口")
@RequestMapping("/app")
@AllArgsConstructor
public class LotteryController extends BaseController {

    /**
     * 可进行的抽奖列表
     */
    @PreAuthorize("hasAuthority('lottery')")
    @GetMapping("/lottery/list")
    public ResponseDTO<?> lotteryList() {
        return ResponseDTO.ok();
    }

    /**
     * 可进行的抽奖列表
     */
    @PreAuthorize("hasAuthority('lottery')")
    @PostMapping("/lottery")
    public ResponseDTO<?> lottery() {
        return ResponseDTO.ok();
    }
}
