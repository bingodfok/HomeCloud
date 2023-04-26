package com.cobin.homecloud.common.exception.exceptionhandler;


import com.cobin.homecloud.common.exception.*;
import com.cobin.homecloud.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    Logger logger = LoggerFactory.getLogger(ExceptionsHandler.class);

    /**
     * 注册异常处理
     *
     * @param e RegisterException
     */
    @ExceptionHandler(RegisterException.class)
    public Result handlerRegisterException(RegisterException e) {
        return Result.error(e.getCode(), e.getCustomMsg());
    }

    /**
     * 手机号异常处理
     *
     * @param e PhoneFormException
     */
    @ExceptionHandler(PhoneFormException.class)
    public Result handlePhoneFormException(PhoneFormException e) {
        return Result.error(e.getCode(), e.getCustomMsg());
    }

    @ExceptionHandler(CaptchaOvertimeException.class)
    public Result handleCaptchaOvertimeException(CaptchaOvertimeException e) {
        return Result.error(e.getCode(), e.getCustomMsg());
    }

    /**
     * 参数检测异常
     */
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        return Result.error(500, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 密码错误异常
     */
    @ExceptionHandler(PassErrorException.class)
    public Result handlePassErrorException(PassErrorException e) {
        return Result.error(e.getCode(), e.getCustomMsg());
    }

    /**
     * 业务服务异常错误
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException e) {
        return Result.error(e.getCode(), e.getCustomMsg());
    }

    @ExceptionHandler(LoginInfoErrorException.class)
    public Result handleLoginInfoException(LoginInfoErrorException e) {
        return Result.error(e.getCode(), e.getCustomMsg());
    }

    /**
     * 重复登录异常
     */
    @ExceptionHandler(RepeatLoginException.class)
    public Result handleRepeatLoginException(RepeatLoginException e) {
        return Result.error(e.getCode(), e.getCustomMsg());
    }

//    @ExceptionHandler(MqttException.class)
//    public void handleMqttException(MqttMessage e){
//        logger.warn("Mqtt连接异常");
//    }

}
