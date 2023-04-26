package com.cobin.homecloud.common.exception;

/**
 * 用户注册异常
 *
 * @Author 1_bit
 * @Date 2023/3/13 23:25
 */
public class RegisterException extends BaseException {

    private final static String REGISTER_MODULE = "new user registration module";

    public RegisterException(String msg, String module, Integer code, String keyword, Object[] args) {
        super(msg, module, code, keyword, args);
    }

    public RegisterException(String module, Integer code, String keyword, Object[] args) {
        super(null, module, code, keyword, args);
    }

    public RegisterException(Integer code, String keyword) {
        super((String) null, REGISTER_MODULE, code, keyword, (Object) null);
    }

}
