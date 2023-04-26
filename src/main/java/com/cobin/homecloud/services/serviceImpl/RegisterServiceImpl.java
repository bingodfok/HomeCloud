package com.cobin.homecloud.services.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cobin.homecloud.common.entity.SysUser;
import com.cobin.homecloud.common.exception.RegisterException;
import com.cobin.homecloud.common.exception.ServiceException;
import com.cobin.homecloud.common.vo.RegisterInfo;
import com.cobin.homecloud.mapper.UserMapper;
import com.cobin.homecloud.services.RegisterService;
import com.cobin.homecloud.utils.CaptchaUtils;
import com.cobin.homecloud.utils.RedisUtils;
import com.cobin.homecloud.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RegisterServiceImpl implements RegisterService {

    Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
    @Resource
    UserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void RegisterEntry(RegisterInfo registerInfo) {
        SysUser sysUser = new SysUser(registerInfo);
        try {
            userMapper.insert(sysUser);
        } catch (Exception e) {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", registerInfo.getPhone());
            if (!userMapper.selectList(wrapper).isEmpty()) {
                logger.info("手机号：{}，用户注册失败", registerInfo.getPhone());
                throw new RegisterException(1001, "register.phone.repeat");
            } else {
                e.printStackTrace();
                //未知系统异常
                throw new ServiceException(1008, "用户注册出现异常");
            }
        }
    }

    @Override
    public boolean RegisterInfoHandler(RegisterInfo registerInfo) {
        CaptchaUtils.ValidatePhone(registerInfo.getPhone());
        String captcha = (String) RedisUtils.getValue(CaptchaUtils.SMS_PATH_PREFIX + registerInfo.getPhone());
        if (!StrUtils.isEmpty(captcha)) {
            RedisUtils.deleteValue(CaptchaUtils.SMS_PATH_PREFIX + registerInfo.getPhone());
            if (registerInfo.getCaptcha().equals(captcha)) {
                passwordEncryption(registerInfo);
                RegisterEntry(registerInfo);
            } else {
                logger.info("手机号：{}，用户验证码错误", registerInfo.getPhone());
                throw new RegisterException(1000, "common.captcha.error");
            }
        } else {
            throw new RegisterException(1003, "common.captcha.timeout");
        }
        return true;
    }

    /**
     * 用户名规范性检测
     */
    public boolean UserNameCheck(String name) {
        return name.length() <= 7;
    }

    /**
     * 用户密码加密
     *
     * @param registerInfo 用户注册信息
     */
    public void passwordEncryption(final RegisterInfo registerInfo) {
        registerInfo.setPassword(bCryptPasswordEncoder.encode(registerInfo.getPassword()));
    }
}
