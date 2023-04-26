package com.cobin.homecloud.utils;

import com.cobin.homecloud.common.entity.UserDetailImpl;
import com.cobin.homecloud.common.exception.TokenVitiationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Component
public class JwtUtils {
    Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    public static final String login_info_path_prefix = "login_info:";
    public static final String token_key = "token_key";
    private static String secret;
    public static long expireTime;
    private static String header;
    private static final String token_prefix = "Bearer ";
    public static final Long MINUTE = 1000 * 60L;

    @Value("${token.secret}")
    public void setSecret(String secret) {
        JwtUtils.secret = secret;
    }

    @Value("${token.expireTime}")
    public void setExpireTime(long expireTime) {
        JwtUtils.expireTime = expireTime;
    }

    @Value("${token.header}")
    public void setHeader(String header) {
        JwtUtils.header = header;
    }

    /**
     * 生成token
     *
     * @param userUID uuid(任意用户唯一标识) uuid 相同时生成的token相同（使用固定信息(手机号)生成uuid有安全风险）
     * @return token
     */
    public String createToken(String userUID) {
        HashMap<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        DefaultClaims claims = new DefaultClaims();
        claims.put(token_key, userUID);
        return Jwts.builder().setHeader(header)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * token 解析
     *
     * @param token token
     * @return uuid
     */
    public String parseToken(String token) {
        String TokenContext = null;
        if (StrUtils.notEmpty(token)) {
            try {
                TokenContext = (String) Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token)
                        .getBody()
                        .get(token_key);
            } catch (Exception e) {
                //token 无效
                throw new TokenVitiationException();
            }
        }
        return TokenContext;
    }

    /**
     * 刷新登录信息有效时间
     */
    public UserDetailImpl refreshExpireTime(UserDetailImpl userDetail) {
        long differ = userDetail.getExpireTime() - System.currentTimeMillis();
        //小于10分钟刷新
        if (differ < 10 * MINUTE) {
            userDetail.setExpireTime(System.currentTimeMillis() + MINUTE * expireTime);
            RedisUtils.setValueTimeout(login_info_path_prefix + userDetail.getUsername(), userDetail, expireTime);
        }
        return userDetail;
    }

    /**
     * @param request 请求
     * @return 登录用户信息
     */
    public UserDetailImpl getLoginUserByToken(HttpServletRequest request) {
        UserDetailImpl userDetail = null;
        //获取token
        String info = request.getHeader(JwtUtils.header);
        if (info != null && info.startsWith(token_prefix)) {
            String token = info.replace(token_prefix, "");
            //获取token中的用户标识UUID
            String userUID = parseToken(token);
            //从Redis中读取用户信息
            userDetail = (UserDetailImpl) RedisUtils.getValue(login_info_path_prefix + userUID);
            //用户token未过期，检查到期时间如果小于10分刷新token有效时间
            if (userDetail != null) {
                userDetail = refreshExpireTime(userDetail);
            }
        }
        return userDetail;
    }
}
