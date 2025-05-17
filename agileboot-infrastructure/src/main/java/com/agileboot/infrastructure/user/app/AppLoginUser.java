package com.agileboot.infrastructure.user.app;

import com.agileboot.infrastructure.user.base.BaseLoginUser;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * 登录用户身份权限
 * @author valarchie
 */
@Data
@NoArgsConstructor
public class AppLoginUser extends BaseLoginUser {

    private static final long serialVersionUID = 1L;

    private boolean isVip;


    public AppLoginUser(Long userId, Boolean isVip, String cachedKey) {
        this.cachedKey = cachedKey;
        this.userId = userId;
        this.isVip = isVip;
    }

    public AppLoginUser(Long userId) {
        this.userId = userId;
        this.username = String.valueOf(userId);
        this.authorities = Lists.newArrayList((GrantedAuthority) () -> "lottery");
    }
}
