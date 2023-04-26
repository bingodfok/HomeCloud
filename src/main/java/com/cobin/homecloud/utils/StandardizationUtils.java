package com.cobin.homecloud.utils;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 标准化工具
 *
 * @Author 1_bit
 * @Date 2023/3/14 19:19
 */

public class StandardizationUtils {
    /**
     * @param keyword 标准化 keyword
     * @param args    外部参数
     * @return 标准化返回结果
     */
    public static String getContent(String keyword, Object... args) {

        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        //LocaleContextHolder.getLocale() 本地化处理对象
        return messageSource.getMessage(keyword, args, LocaleContextHolder.getLocale());
    }
}
