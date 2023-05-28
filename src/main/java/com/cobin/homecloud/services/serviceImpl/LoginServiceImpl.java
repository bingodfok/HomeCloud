package com.cobin.homecloud.services.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cobin.homecloud.common.entity.SysUser;
import com.cobin.homecloud.common.entity.UserDetailImpl;
import com.cobin.homecloud.common.exception.*;
import com.cobin.homecloud.common.vo.LoginInfo;
import com.cobin.homecloud.mapper.UserMapper;
import com.cobin.homecloud.services.LoginService;
import com.cobin.homecloud.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 用户登录服务
 *
 * @Author 1_bit
 * @Date 2023/3/28 9:30
 */
@Service
public class LoginServiceImpl implements LoginService {

    public static String USER_INFO_PATH_PREFIX = "login_info:";

    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    public static String LOGIN_SERVICE_MODE = "login service mode";

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtUtils jwtUtils;

    public String login(LoginInfo loginInfo) {
        UserDetailImpl userDetail = null;
        switch (loginInfo.getMode()) {
            case 0:
                userDetail = ValidateLogonCaptcha(loginInfo);
                break;
            case 1:
                userDetail = ValidateLoginPass(loginInfo);
        }
        if (userDetail != null) {
            //检查当前登录状态  开发阶段不考虑多设备登录 当前禁止多次登录
            if (LoginStatus(loginInfo)) {
                throw new RepeatLoginException();
            }
            //生成token
            userDetail.setToken(jwtUtils.createToken(userDetail.getUsername()));
            //设置登录信息失效时间
            userDetail.setExpireTime(System.currentTimeMillis() + JwtUtils.expireTime * JwtUtils.MINUTE);
            //将登录用户信息保存于缓存中
            RedisUtils.setValueTimeout(USER_INFO_PATH_PREFIX + userDetail.getUsername(), userDetail, JwtUtils.expireTime);
            logger.info("用户{}登录成功", userDetail.getUsername());
            return userDetail.getToken();
        }
        return null;
    }

    /**
     * 验证码认证
     */
    @Override
    public UserDetailImpl ValidateLogonCaptcha(final LoginInfo loginInfo) {
        UserDetailImpl userDetail;
        CaptchaUtils.ValidatePhone(loginInfo.getPhone());
        String value = (String) RedisUtils.getValue(CaptchaUtils.SMS_PATH_PREFIX + loginInfo.getPhone());

        if (value != null) {
            RedisUtils.deleteValue(CaptchaUtils.SMS_PATH_PREFIX + loginInfo.getPhone());
        }

        if (StrUtils.isEmpty(value)) {
            throw new CaptchaOvertimeException(1005, "common.captcha.timeout");
        } else if (value.equals(loginInfo.getCaptcha())) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getPhone, loginInfo.getPhone());
            SysUser sysUser = userMapper.selectOne(wrapper);
            if (sysUser == null) {
                throw new LoginInfoErrorException(1005, "login.info.unregistered", loginInfo.getPhone());
            }
            userDetail = new UserDetailImpl();
            userDetail.setSysUser(sysUser);
            return userDetail;
        }
        return null;
    }

    /**
     * 密码认证
     */
    @Override
    public UserDetailImpl ValidateLoginPass(final LoginInfo loginInfo) {
        CaptchaUtils.ValidatePhone(loginInfo.getPhone());
        Authentication authenticate;
        // RSA 密码解密
        String password = RsaUtil.decryStr(loginInfo.getPassword(), StandardCharsets.UTF_8);
        //验证用户密码和手机号
        try {
            UsernamePasswordAuthenticationToken AuthenticationToken = new UsernamePasswordAuthenticationToken(loginInfo.getPhone(),password);
            authenticate = authenticationManager.authenticate(AuthenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                //密码错误
                throw new PassErrorException(1006, "login.info.passworderror");
            } else {
                //其他异常 用户为注册
                throw new ServiceException(LOGIN_SERVICE_MODE, 1005, "login.info.unregistered", loginInfo.getPhone());
            }
        }
        return (UserDetailImpl) authenticate.getPrincipal();
    }

    /**
     * 检查用户是否处于登录状态 是true 否false
     */
    boolean LoginStatus(LoginInfo loginInfo) {
        UserDetailImpl userDetail = (UserDetailImpl) RedisUtils.getValue(JwtUtils.login_info_path_prefix + loginInfo.getPhone());
        return userDetail != null;
    }
}
