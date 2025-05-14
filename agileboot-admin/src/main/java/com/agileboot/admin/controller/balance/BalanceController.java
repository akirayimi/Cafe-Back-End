package com.agileboot.admin.controller.balance;

import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.domain.biz.balance.command.ConsumeCommand;
import com.agileboot.domain.biz.balance.command.RechargeCommand;
import com.agileboot.domain.biz.balance.db.BalanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "余额API", description = "余额相关接口")
@RestController
@RequestMapping("/balance")
@Slf4j
public class BalanceController {
    @Autowired
    private BalanceService balanceService;

    @PostMapping("/recharge")
    @PreAuthorize("@permission.has('stuff:balance')")
    public ResponseDTO recharge(@RequestBody @Validated RechargeCommand command) {
        boolean result = balanceService.recharge(command);
        return ResponseDTO.ok();
    }

    @PostMapping("/consume")
    @PreAuthorize("@permission.has('stuff:balance')")
    public ResponseDTO consume(@RequestBody @Validated ConsumeCommand command) {
        boolean result = balanceService.consume(command);
        return ResponseDTO.ok();
    }

    @GetMapping("/log")
    @PreAuthorize("@permission.has('stuff:balance')")
    public ResponseDTO log(String customerId) {
        return ResponseDTO.ok(balanceService.log(customerId));
    }

}
