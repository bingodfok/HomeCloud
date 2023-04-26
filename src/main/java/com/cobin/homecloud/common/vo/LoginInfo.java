package com.cobin.homecloud.common.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class LoginInfo {

    @NotBlank(message = "手机号不能为空")
    private String phone;
    private String captcha;
    private String password;
    /**
     * 验证模式 0 验证码 1 密码
     */
    @Max(value = 1, message = "验证模式错误")
    @Min(value = 0, message = "验证模式错误")
    private Integer mode;
}
