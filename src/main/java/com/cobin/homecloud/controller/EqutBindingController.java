package com.cobin.homecloud.controller;


import com.cobin.homecloud.common.entity.Equtpment;
import com.cobin.homecloud.common.exception.ServiceException;
import com.cobin.homecloud.common.vo.Result;
import com.cobin.homecloud.services.EqutService;
import com.cobin.homecloud.services.serviceImpl.EquipmentBindingServiceImpl;
import com.cobin.homecloud.utils.StrUtils;
import com.cobin.homecloud.utils.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 设备绑定相关 Controller
 *
 * @Author 1_bit
 * @Date 2023/4/24 23:55
 */
@RestController
@RequestMapping("/system/equt_binding")
public class EqutBindingController {

    @Autowired
    EqutService equtService;

    @Autowired
    EquipmentBindingServiceImpl bindingService;

    /**
     * 设备绑定前获取设备基本信息
     *
     * @param hcid 设备码
     * @return Result
     */
    @GetMapping("/equt_info/{hcid}")
    public Result getEqutInfo(@PathVariable String hcid) {
        if (!checkEqutName(hcid)) {
            return Result.error(2002, "请求参数异常");
        } else {
            Equtpment equtInfo = bindingService.getEqutInfo(hcid);
            if (equtInfo == null) {
                throw new ServiceException(2003, "system.equt.device_exist", hcid);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("equtCode", equtInfo.getEqutCode());
                map.put("disNetPass", equtInfo.getDisNetPass());
                map.put("disNetSsid", equtInfo.getDisNetSsid());
                map.put("equtStatus", equtInfo.getEqutStatus());
                map.put("bindingCode", ThreadContext.getContext());
                ThreadContext.remove();
                return Result.success(200, "获取设备信息成功", map);
            }
        }
    }

    /**
     * 获取设备绑定所需信息
     */
    @GetMapping("/equt_binding_info/{hcid}/{bindingCode}")
    public Result genEqutInfo(@PathVariable String hcid, @PathVariable String bindingCode) {
        if (checkEqutName(hcid) && bindingCode != null) {
            return Result.success(bindingService.GenerateBindingInfo(hcid, bindingCode));
        } else {
            return Result.error(2002, "请求参数异常");
        }
    }

    /**
     * hcid参数检验
     */
    public boolean checkEqutName(String hcid) {
        return !StrUtils.isEmpty(hcid) && Pattern.matches("^HC-[A-Z]{0,10}-[0-9]{9}$", hcid);
    }
}
