package com.cobin.homecloud.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cobin.homecloud.common.entity.SysFile;

public interface SysFileService extends IService<SysFile> {

    SysFile getSysFileByUserAndId(String userId,Integer id);
}
