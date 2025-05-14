package com.agileboot.domain.biz.balance.db;

import com.agileboot.domain.biz.balance.dto.CustomerLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:44:16
 */
public interface BalanceLogMapper extends BaseMapper<BalanceLog> {
    List<CustomerLog> log(@Param("customerId") String customerId, @Param("type") Integer type);
}
