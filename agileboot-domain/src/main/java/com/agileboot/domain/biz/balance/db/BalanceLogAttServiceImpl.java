package com.agileboot.domain.biz.balance.db;

import com.agileboot.domain.biz.balance.command.ConsumeCommand;
import com.agileboot.infrastructure.user.AuthenticationUtils;
import com.agileboot.infrastructure.user.web.SystemLoginUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2025-04-25 14:44:23
 */
@Service
public class BalanceLogAttServiceImpl extends ServiceImpl<BalanceLogAttMapper, BalanceLogAtt> implements BalanceLogAttService {

    @Override
    public void createAtt(Long id, ConsumeCommand command) {
        BalanceLogAtt att = new BalanceLogAtt();
        att.setLogId(id);
        att.setSerialNum(command.getSerialNumber());
        att.setAttachment(command.getAttachment());
        att.setProduct(command.getProduct());
        save(att);
    }
}
