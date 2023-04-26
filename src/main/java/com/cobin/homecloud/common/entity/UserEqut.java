package com.cobin.homecloud.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户_设备
 *
 * @Author 1_bit
 * @Date 2023/4/24 22:48
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user_equt")
public class UserEqut extends BaseEntity {
    /**
     * 用户设备表id
     */
    @TableId
    private Integer ueId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 设备ID
     */
    private Integer equtId;
    /**
     * 控制话题
     */
    private String controlTopic;
    /**
     * 状态话题
     */
    private String statusTopic;
    /**
     * 反馈话题
     */
    private String feedbackTopic;
    /**
     * 在线状态
     */
    private String onlineStatus;

}
