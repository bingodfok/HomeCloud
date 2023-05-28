package com.cobin.homecloud.common.config;

import com.cobin.homecloud.common.security.AccessDeniedHandlerImpl;
import com.cobin.homecloud.common.security.AuthenticationEntryPointImpl;
import com.cobin.homecloud.common.security.JwtAuthenticationFilter;
import com.cobin.homecloud.services.serviceImpl.UserDetailsServiceImpl;
import com.cobin.homecloud.utils.AnonymousUrlContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * SecurityConfig
 *
 * @Author 1_bit
 * @Date 2023/3/29 16:58
 */
@EnableWebSecurity
@SuppressWarnings("all")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Resource(name = "AccessDeniedHandlerImpl")
    AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    AnonymousUrlContext anonymousUrlContext;

    @Bean
    public AuthenticationManager SetAuthenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry Registry = http.authorizeRequests();
        anonymousUrlContext.getAnonymousUrl().forEach(url -> Registry.mvcMatchers(url).permitAll());
        // 由于用token验证用户 取消从Session获取用户信息
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/websocket/equt_binding/*").permitAll()
                .anyRequest().authenticated();
        //jwt过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //自定义（授权失败）无权访问处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        //自定义认证失败异常处理
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(BCryptPasswordEncoderBean());
    }

    @Bean
    public BCryptPasswordEncoder BCryptPasswordEncoderBean() {
        return new BCryptPasswordEncoder();
    }
}
