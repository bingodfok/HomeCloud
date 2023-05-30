package com.cobin.homecloud.controller;

import cn.hutool.core.codec.Base64;
import com.cobin.homecloud.common.annotation.Anonymous;
import com.cobin.homecloud.common.vo.LoginInfo;
import com.cobin.homecloud.common.vo.RegisterInfo;
import com.cobin.homecloud.common.vo.Result;
import com.cobin.homecloud.services.LoginService;
import com.cobin.homecloud.services.RegisterService;
import com.cobin.homecloud.utils.CaptchaUtils;
import com.cobin.homecloud.utils.RsaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/system/common")
public class CommonController {

    Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    RegisterService registerService;

    @Autowired
    LoginService loginService;

    /**
     * 用户注册
     *
     * @return 注册信息
     */
    @Anonymous
    @PostMapping("/register")
    public Result RegisterController(@RequestBody RegisterInfo registerInfo) {
        if (registerService.RegisterInfoHandler(registerInfo)) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    /**
     * 用户登录
     *
     * @param loginInfo 登录信息
     */
    @Anonymous
    @PostMapping("/login")
    public Result LoginController(@RequestBody @Validated LoginInfo loginInfo) {
        logger.info("手机号{}，验证码{},密码{}", loginInfo.getPhone(), loginInfo.getCaptcha(), loginInfo.getPassword());
        String token = loginService.login(loginInfo);
        if (token != null) {
            return Result.success().put("token", token);
        }
        return Result.error();
    }

    /**
     * 获取短信验证码
     *
     * @return 开发阶段返回验证码
     */
    @Anonymous
    @GetMapping("/sms_captcha")
    public Result GetCaptchaController(@RequestParam("phone") String phone) {
        logger.info("手机号【{}】获取验证码", phone);
        String smsCaptcha = CaptchaUtils.createSmsCaptcha(phone);
        return Result.success(200, "验证码获取成功", smsCaptcha);
    }

    /**
     * 获取密码加密公钥
     *
     * @return 加密公钥 Base64编码
     */
    @Anonymous
    @GetMapping("/rsa_public_key")
    public Result GetRsaPublicKey() {
        String Base64Key = Base64.encode(RsaUtil.getPublicKey().getEncoded());
        return Result.success().put("publicKey", Base64Key);
    }
}
