package com.cobin.homecloud.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInfo {
    /**
     * 密码
     */
    @Size(max = 10, message = "密码长度超过最大限制10位")
    private String password;
    /**
     * 电话
     */
    private String phone;
    /**
     * 验证码
     */
    private String captcha;
}
