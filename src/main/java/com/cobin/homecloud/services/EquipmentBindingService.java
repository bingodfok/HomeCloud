package com.cobin.homecloud.services;

import com.cobin.homecloud.common.entity.Equtpment;
import com.cobin.homecloud.common.entity.UserEqut;

/**
 * 用户设备绑定服务
 *
 * @Author 1_bit
 * @Date 2023/4/24 23:13
 */
public interface EquipmentBindingService {
    /**
     * 检查设备是否被绑定
     * @return boolean
     */
    boolean checkBinding(String hcid);

    Equtpment getEqutInfo(String hcid);

    /**
     * 生成设备绑定相关参数 topic 等
     */
    UserEqut GenerateBindingInfo(String hcid,String bindingCode);

}
