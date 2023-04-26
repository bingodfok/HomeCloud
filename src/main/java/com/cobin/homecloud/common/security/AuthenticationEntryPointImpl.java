package com.cobin.homecloud.common.security;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.cobin.homecloud.common.vo.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义未登录异常处理
 *
 * @Author 1_bit
 * @Date 2023/3/29 16:51
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result result = new Result(HttpStatus.HTTP_UNAUTHORIZED, "用户未登录，无权访问该连接");
        response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSONUtil.parseObj(result).toStringPretty());
    }
}
