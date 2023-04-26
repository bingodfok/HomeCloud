package com.cobin.homecloud.common.exception;

public class CaptchaOvertimeException extends BaseException {

    public CaptchaOvertimeException(String msg, String module, Integer code, String keyword, Object... args) {
        super(msg, module, code, keyword, args);
    }

    public CaptchaOvertimeException(Integer code, String keyword, Object... args) {
        super(null, null, code, keyword, args);

    }
}
