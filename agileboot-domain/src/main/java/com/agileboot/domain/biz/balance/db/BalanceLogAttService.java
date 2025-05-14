package com.agileboot.domain.biz.balance.db;

import com.agileboot.domain.biz.balance.command.ConsumeCommand;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:44:23
 */
public interface BalanceLogAttService extends IService<BalanceLogAtt> {

    void createAtt(Long id, ConsumeCommand command);
}
