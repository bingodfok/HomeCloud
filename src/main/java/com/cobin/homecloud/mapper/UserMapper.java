package com.cobin.homecloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cobin.homecloud.common.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

}
