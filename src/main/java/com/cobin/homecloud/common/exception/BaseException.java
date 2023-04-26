package com.cobin.homecloud.common.exception;

import com.cobin.homecloud.utils.StandardizationUtils;
import com.cobin.homecloud.utils.StrUtils;

/**
 * 基本异常
 *
 * @Author 1_bit
 * @Date 2023/3/13 23:24
 */

public class BaseException extends RuntimeException {
    /**
     * 异常消息
     */
    private String msg;
    /**
     * 模块
     */
    private String module;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 关键词参数
     */
    private Object[] args;

    public BaseException(String msg, String module, Integer code, String keyword, Object... args) {
        this.msg = msg;
        this.module = module;
        this.code = code;
        this.keyword = keyword;
        this.args = args;
    }

    public BaseException(String module, Integer code, String keyword, Object... args) {
        this.args = args;
        this.keyword = keyword;
        this.code = code;
        this.module = module;
    }

    public BaseException() {

    }

    public BaseException(String msg, String module, Integer code) {
        this(msg, module, code, null);
    }

    public BaseException(Integer code, String keyword, Object... args) {
        this(null, null, code, keyword, args);
    }

    public BaseException(String module, String keyword, Object... args) {
        this(null, module, null, keyword, args);
    }

    public BaseException(String keyword, Object... args) {
        this(null, null, null, keyword, args);
    }

    public BaseException(Integer code, String msg) {
        this(msg, null, code, null);
    }


    /**
     * 获取自定义消息
     * 自定义消息和keyword不能同时为空 优先返回标准化消息
     *
     * @return 异常消息
     */
    public String getCustomMsg() {
        if (StrUtils.isEmpty(keyword)) {
            return msg;
        } else {
            return StandardizationUtils.getContent(keyword, args);
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }


    public String getModule() {
        return module;
    }

    public Integer getCode() {
        return code;
    }

    public String getKeyword() {
        return keyword;
    }

    public Object[] getArgs() {
        return args;
    }
}
