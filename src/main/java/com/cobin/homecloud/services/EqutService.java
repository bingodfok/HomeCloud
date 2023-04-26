package com.cobin.homecloud.services;

import com.cobin.homecloud.common.entity.Equtpment;

/**
 * @Author 1_bit
 * @Date 2023/4/24 23:31
 */
public interface EqutService {
     /**
      * 根据设备码获取设备信息
      * @return Equtpment
      */
     Equtpment getEqutInfoByEqutCode(String hcid);


}
