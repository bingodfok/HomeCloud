package com.cobin.homecloud.services.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cobin.homecloud.common.entity.SysFile;
import com.cobin.homecloud.mapper.SysFileMapper;
import com.cobin.homecloud.services.SysFileService;
import org.springframework.stereotype.Service;

@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {


    @Override
    public SysFile getSysFileByUserAndId(String userId, Integer id) {
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<>();
        return getOne(wrapper.eq(SysFile::getId, id)
                .eq(SysFile::getUserId, userId));
    }
}
