package com.cobin.homecloud.services.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cobin.homecloud.common.entity.SysUser;
import com.cobin.homecloud.common.entity.UserDetailImpl;
import com.cobin.homecloud.common.exception.LoginInfoErrorException;
import com.cobin.homecloud.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, username);
        SysUser sysUser = userMapper.selectOne(wrapper);
        if (sysUser == null) {
            logger.info("没有用户{}", username);
            throw new LoginInfoErrorException(1005, "login.info.unregistered", username);
        }

        UserDetailImpl userDetail = new UserDetailImpl();
        userDetail.setSysUser(sysUser);
        return userDetail;
    }
}
