package com.cobin.homecloud.common.exception;

/**
 * 手机号格式错误异常
 *
 * @Author 1_bit
 * @Date 2023/3/27 21:20
 */
public class PhoneFormException extends BaseException {

    public PhoneFormException(String msg, String module, Integer code, String keyword, Object... args) {
        super(msg, module, code, keyword, args);
    }

    public PhoneFormException(Object... args) {
        super(null, null, 1004, "common.phone.abnormal", args);
    }
}
