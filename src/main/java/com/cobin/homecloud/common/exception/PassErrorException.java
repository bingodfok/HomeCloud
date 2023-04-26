package com.cobin.homecloud.common.exception;

/**
 * 用户密码错误异常
 *
 * @Author 1_bit
 * @Date 2023/3/28 10:20
 */
public class PassErrorException extends BaseException {
    public PassErrorException(String msg, String module, Integer code, String keyword, Object... args) {
        super(msg, module, code, keyword, args);
    }

    public PassErrorException(Integer code, String keyword) {
        super((String) null, null, code, keyword, (Object) null);
    }
}
