package com.agileboot.admin.controller.customer;


import com.agileboot.common.core.dto.ResponseDTO;
import com.agileboot.domain.biz.customer.command.CustomerQuery;
import com.agileboot.domain.biz.customer.db.Customer;
import com.agileboot.domain.biz.customer.db.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.agileboot.common.core.base.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:43:33
 */
@Validated
@Tag(name = "客户API", description = "客户相关接口")
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController {
    @Autowired
    private CustomerService customerService;

    // 创建客户
    @PostMapping("/create")
    @PreAuthorize("@permission.has('stuff:customer')")
    public ResponseDTO createCustomer(@RequestBody @Validated Customer customer) {
        boolean result = customerService.createCustomerBalance(customer);
        return ResponseDTO.ok();
    }

    @GetMapping("/list")
    @PreAuthorize("@permission.has('stuff:customer')")
    public ResponseDTO listCustomer(CustomerQuery command) {
        return ResponseDTO.ok(customerService.listCustomerBalance(command));
    }
}

