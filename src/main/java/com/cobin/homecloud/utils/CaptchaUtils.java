package com.cobin.homecloud.utils;

import com.cobin.homecloud.common.exception.PhoneFormException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信验证码工具
 *
 * @Author 1_bit
 * @Date 2023/3/27 19:23
 */
public class CaptchaUtils {

    public static String SMS_PATH_PREFIX = "sms_captcha:";

    public static String createSmsCaptcha(String phone) {
        ValidatePhone(phone);
        String smsCaptcha = creteCaptcha(5);
        SendSmsCaptcha(smsCaptcha);
        RedisUtils.setValueTimeout(SMS_PATH_PREFIX + phone, smsCaptcha, 2L);
        return smsCaptcha;
    }

    /**
     * 向验证码平台 发送短信验证码
     */
    private static void SendSmsCaptcha(String captcha) {

    }

    /**
     * 生成指定位数 数字验证码
     *
     * @param num 位数
     * @return 验证码
     */
    public static String creteCaptcha(int num) {
        return "43671";
    }

    /**
     * 验证手机号合法性
     *
     * @param phone 手机号
     */
    public static void ValidatePhone(String phone) {
        Pattern compile = Pattern.compile("^1[3-9][0-9]{9}$");
        Matcher matcher = compile.matcher(phone);
        if (!matcher.matches()) {
            throw new PhoneFormException(phone);
        }
    }

}
