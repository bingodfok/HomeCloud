package com.cobin.homecloud.common.exception;

/**
 * 登录信息错误异常
 *
 * @Author 1_bit
 * @Date 2023/3/27 23:58
 */
public class LoginInfoErrorException extends BaseException {
    public LoginInfoErrorException(String msg, String module, Integer code, String keyword, Object... args) {
        super(msg, module, code, keyword, args);
    }

    public LoginInfoErrorException(Integer code, String keyword, Object... args) {
        super(null, null, code, keyword, args);

    }

}
