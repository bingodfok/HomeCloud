package com.cobin.homecloud.services.serviceImpl;

import com.cobin.homecloud.common.entity.Equtpment;
import com.cobin.homecloud.common.entity.UserDetailImpl;
import com.cobin.homecloud.common.entity.UserEqut;
import com.cobin.homecloud.common.exception.ServiceException;
import com.cobin.homecloud.services.EquipmentBindingService;
import com.cobin.homecloud.services.EqutService;
import com.cobin.homecloud.utils.RedisUtils;
import com.cobin.homecloud.utils.StrUtils;
import com.cobin.homecloud.utils.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * @Author 1_bit
 * @Date 2023/4/25 0:30
 */
@Service
public class EquipmentBindingServiceImpl implements EquipmentBindingService {

    @Autowired
    private EqutService equtService;

    public String USER_EQUT_CACHE_PREFIX = "user_equt_info:";
    /**
     * 临时绑定验证码
     */
    public String BINDING_CAPTCHA_CODE_CACHE_PREFIX = "binding_captcha_code:";

    /**
     * 检查设备是否被绑定
     */
    @Override
    public boolean checkBinding(String hcid) {
        return Objects.equals(equtService.getEqutInfoByEqutCode(hcid).getEqutStatus(), "1");
    }

    /**
     * 获取待绑定设备信息
     */
    @Override
    public Equtpment getEqutInfo(String hcid) {
        Equtpment equtInfo = equtService.getEqutInfoByEqutCode(hcid);
        if (equtInfo != null && "0".equals(equtInfo.getEqutStatus())) {
            String code = UUID.randomUUID().toString();

            RedisUtils.setValueTimeout(BINDING_CAPTCHA_CODE_CACHE_PREFIX + hcid, code, 5L);
            ThreadContext.setContext(code);
            return equtInfo;
        } else if (equtInfo != null) {
            throw new ServiceException(2001, "system.equt.is_binding", hcid);
        }
        return null;
    }

    /**
     * 生成设备绑定相关数据 并保存到Redis缓存
     *
     * @param hcid 设备码
     */
    @Override
    public UserEqut GenerateBindingInfo(String hcid, String bindingCode) {

        String value = (String) RedisUtils.getValue(BINDING_CAPTCHA_CODE_CACHE_PREFIX + hcid);
        if (StrUtils.isEmpty(value)) {
            throw new ServiceException(2005, "system.equt.equt_code_exception", hcid);
        }
        if (bindingCode != null && bindingCode.equals(value)) {
            UserEqut userEqut = new UserEqut();
            UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userEqut.setUserId(principal.getSysUser().getUserId());
            Equtpment equtInfo = equtService.getEqutInfoByEqutCode(hcid);

            userEqut.setEqutId(equtInfo.getEqutId());
            userEqut.setControlTopic("control_topic/" + genTopicSuffix());

            userEqut.setStatusTopic("status_topic/bing");
            userEqut.setFeedbackTopic("feedback_topic/" + genTopicSuffix());
            RedisUtils.setValue(USER_EQUT_CACHE_PREFIX + hcid, userEqut);
            return userEqut;
        } else {
            throw new ServiceException(2004, "system.equt.equt_binding_code_exception", bindingCode);
        }
    }

    /**
     * 生成Mqtt话题后缀
     *
     * @return Suffix
     */
    private String genTopicSuffix() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}
