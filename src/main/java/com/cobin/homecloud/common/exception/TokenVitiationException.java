package com.cobin.homecloud.common.exception;

public class TokenVitiationException extends BaseException {

    public TokenVitiationException(String module, Integer code, String keyword, Object... args) {
        super(module, code, keyword, args);
    }

    public TokenVitiationException(Object... args) {
        super(1009, "system.token.vitiation", args);
    }
}
