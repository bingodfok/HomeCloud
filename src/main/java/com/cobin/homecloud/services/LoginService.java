package com.cobin.homecloud.services;

import com.cobin.homecloud.common.entity.UserDetailImpl;
import com.cobin.homecloud.common.vo.LoginInfo;

/**
 * 登录服务接口
 *
 * @Author 1_bit
 * @Date 2023/3/27 21:51
 */
public interface LoginService {

    String login(LoginInfo loginInfo);

    /**
     * 登录验证(密码)
     */
    UserDetailImpl ValidateLogonCaptcha(LoginInfo loginInfo);

    /**
     * 登录验证(验证码)
     */
    UserDetailImpl ValidateLoginPass(LoginInfo loginInfo);

}
