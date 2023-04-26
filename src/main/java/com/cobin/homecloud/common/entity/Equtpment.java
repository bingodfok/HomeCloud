package com.cobin.homecloud.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 设备实体
 *
 * @Author 1_bit
 * @Date 2023/4/24 22:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_equt")
public class Equtpment extends BaseEntity {
    /**
     * 设备id
     */
    @TableId(type = IdType.AUTO)
    private Integer equtId;
    /**
     * 设备码
     */
    private String equtCode;
    /**
     * 配网密码
     */
    @TableField(value = "dms_pass")
    private String disNetPass;
    /**
     * 配网名
     */
    @TableField(value = "dms_name")
    private String disNetSsid;
    /**
     * 设备状态
     */
    private String equtStatus;
    /**
     * 设备Mac
     */
    private String equtMac;
}
