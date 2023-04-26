package com.cobin.homecloud.common.vo;

import cn.hutool.http.HttpStatus;

import java.util.HashMap;

/**
 * 通用请求响应体
 *
 * @Author 1_bit
 * @Date 2023/3/13 21:49
 */
public class Result extends HashMap<String, Object> {
    private final static String CODE = "code";
    private final static String MSG = "msg";

    private final static String DATA = "data";

    public Result() {
    }

    public Result(Integer code, String msg, Object data) {
        super.put(CODE, code);
        super.put(MSG, msg);
        super.put(DATA, data);
    }

    public Result(Integer code, String msg) {
        put(CODE, code);
        put(MSG, msg);
    }

    public Result put(String key, Object val) {
        super.put(key, val);
        return this;
    }

    public static Result success() {
        return new Result(HttpStatus.HTTP_OK, "请求处理成功");
    }
    public static Result success(Object obj){
        return new Result(HttpStatus.HTTP_OK,"请求处理成功",obj);
    }

    public static Result success(Integer code, String msg) {
        return new Result(code, msg);
    }

    public static Result success(Integer code, String msg, Object obj) {
        return new Result(code, msg, obj);
    }

    public static Result error() {
        return new Result(HttpStatus.HTTP_INTERNAL_ERROR, "请求发生错误");
    }

    public static Result error(Integer code, String msg) {
        return new Result(code, msg);
    }

    public static Result error(Integer code, String msg, Object obj) {
        return new Result(code, msg, obj);
    }


}
