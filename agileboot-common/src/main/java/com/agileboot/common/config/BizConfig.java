package com.agileboot.common.config;

import com.agileboot.common.constant.Constants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

/**
 * 读取项目相关配置
 * TODO 移走  不合适放在这里common包底下
 * @author valarchie
 */
@Component
@ConfigurationProperties(prefix = "biz")
@Data
public class BizConfig {

    /**
     * 项目名称
     */
    private Set<String> fullDataAccessRoleKey;

}
