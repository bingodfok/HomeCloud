package com.cobin.homecloud.services;

import com.cobin.homecloud.common.vo.RegisterInfo;

/**
 * 注册信息服务接口
 *
 * @Author 1_bit
 * @Date 2023/3/13 23:00
 */
public interface RegisterService {
    /**
     * 注册新消息录入
     */
    void RegisterEntry(RegisterInfo registerInfo);

    boolean RegisterInfoHandler(RegisterInfo registerInfo);


}
