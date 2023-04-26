package com.cobin.homecloud.common.security;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.cobin.homecloud.common.vo.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义无权访问异常
 *
 * @Author 1_bit
 * @Date 2023/3/29 15:59
 */
@Component("AccessDeniedHandlerImpl")
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result result = new Result(HttpStatus.HTTP_FORBIDDEN, "权限不足,无法访问该链接。");
        response.setStatus(HttpStatus.HTTP_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSONUtil.parseObj(result).toStringPretty());
    }
}
