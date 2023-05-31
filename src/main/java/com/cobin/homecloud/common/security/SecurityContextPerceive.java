package com.cobin.homecloud.common.security;


import com.cobin.homecloud.common.entity.UserDetailImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security 上下文获取
 *
 * @Author 1_bit
 * @Date 2023/5/31 18:28
 */
public class SecurityContextPerceive {
    /**
     * 获取 SecurityContext
     *
     * @return SecurityContext
     */
    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    /**
     * 获取上下文用户
     *
     * @return UserDetailImpl
     */
    public static UserDetailImpl SecurityUser() {
        return (UserDetailImpl) getSecurityContext().getAuthentication().getPrincipal();
    }
}
