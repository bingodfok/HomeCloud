package com.cobin.homecloud.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cobin.homecloud.common.vo.RegisterInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author 1_bit
 * @Date 2023/3/13 19:06
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "sys_user")
public class SysUser extends BaseEntity {
    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private String userId;
    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;
    /**
     * 密码
     */
    @TableField(value = "password")
    private String passWord;
    /**
     * 性别
     */
    @TableField(value = "sex")
    private String sex;
    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;
    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;
    /**
     * 账号状态
     */
    @TableField(value = "status")
    private String status;
    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;

    public SysUser(RegisterInfo registerInfo) {
        this.passWord = registerInfo.getPassword();
        this.phone = registerInfo.getPhone();
    }

}
