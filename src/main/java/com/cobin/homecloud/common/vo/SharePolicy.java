package com.cobin.homecloud.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharePolicy {
    /**
     * 分享模式  0 密码模式 1 自动过期模式
     */
    private int mode;
    /**
     * 文件id
     */
    @JsonProperty("file_id")
    private Integer fileId;
    /**
     * 分享密码
     */
    @JsonProperty("share_pass")
    private String sharePass;
    /**
     * 过期时间 单位秒
     */
    @JsonProperty("expiry_time")
    private int expiryTime;
    /**
     * 访问次数
     */
    private int num;
}
