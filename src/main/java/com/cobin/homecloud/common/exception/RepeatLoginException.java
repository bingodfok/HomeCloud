package com.cobin.homecloud.common.exception;

/**
 * 用户重复登录异常
 *
 * @Author 1_bit
 * @Date 2023/3/29 13:51
 */
public class RepeatLoginException extends BaseException {

    public RepeatLoginException() {
        super(1010, "system.login.repeatlogin", (Object) null);
    }
}
