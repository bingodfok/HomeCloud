package com.cobin.homecloud.websocket;

import lombok.Data;

/**
 * 设备绑定结果反馈消息
 *
 * @Author 1_bit
 * @Date 2023/4/27 9:48
 */
@Data
public class EqutBindingMsg {
    /**
     * 消息类型
     */
    private String Type;
    /**
     * 状态
     */
    private String status;

}
