package com.agileboot.domain.biz.customer.dto;

import lombok.Data;

@Data
public class AppUserDTO {
    private String nickname;
    private String avatar;
    private String openid;
    private String token;
}
