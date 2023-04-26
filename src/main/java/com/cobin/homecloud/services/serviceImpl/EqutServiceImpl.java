package com.cobin.homecloud.services.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cobin.homecloud.common.entity.Equtpment;
import com.cobin.homecloud.mapper.EquipmentMapper;
import com.cobin.homecloud.services.EqutService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Service
public class EqutServiceImpl implements EqutService {

    @Resource
    EquipmentMapper equipmentMapper;

    @Override
    public Equtpment getEqutInfoByEqutCode(String hcid) {
        Assert.notNull(hcid, "hcid not null");
        return equipmentMapper.selectOne(new LambdaQueryWrapper<Equtpment>().eq(Equtpment::getEqutCode, hcid));
    }
}
