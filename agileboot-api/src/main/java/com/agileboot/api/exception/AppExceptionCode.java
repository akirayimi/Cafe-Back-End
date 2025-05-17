package com.agileboot.api.exception;

import com.agileboot.common.exception.error.ErrorCodeInterface;
import org.springframework.util.Assert;

public enum AppExceptionCode implements ErrorCodeInterface {

    WX_LOGIN_CODE_TO_SESSION_ERROR(1000001, "登录异常，状态码:{}, 信息:{}", "AppExceptionCode.WX_SERVER_ERROR"),
    WX_LOGIN_NOT_BIND(1000002, "用户还未绑定到该小程序", "AppExceptionCode.WX_LOGIN_NOT_BIND"),
    ;

    private final int code;
    private final String msg;

    private final String i18nKey;

    AppExceptionCode(int code, String msg, String i18nKey) {
        Assert.isTrue(code > 1000000 && code < 1000099,
                "错误码code值定义失败，AppExceptionCode错误码code值范围在1000~9999之间，AppExceptionCode，当前错误码码为" + name());

        String errorTypeName = this.getClass().getSimpleName();
        Assert.isTrue(i18nKey != null && i18nKey.startsWith(errorTypeName),
                String.format("错误码i18nKey值定义失败，%s错误码i18nKey值必须以%s开头，当前错误码为%s", errorTypeName, errorTypeName, name()));
        this.code = code;
        this.msg = msg;
        this.i18nKey = i18nKey;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.msg;
    }

    @Override
    public String i18nKey() {
        return this.i18nKey;
    }
}
