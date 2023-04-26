package com.cobin.homecloud.common.exception;

public class ServiceException extends BaseException {

    public ServiceException(String msg, String module, Integer code, String keyword, Object... args) {
        super(msg, module, code, keyword, args);
    }

    public ServiceException(String module, Integer code, String keyword, Object... args) {
        super(module, code, keyword, args);
    }

    public ServiceException(Integer code, String msg) {
        super(code, msg);
    }
    public ServiceException(Integer code, String keyword,Object... args) {
        super(code,keyword, args);
    }
}
