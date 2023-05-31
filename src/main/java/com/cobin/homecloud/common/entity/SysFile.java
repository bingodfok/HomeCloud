package com.cobin.homecloud.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_minio_file")
public class SysFile {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("file_name")
    private String fileName;
    @TableField("file_md")
    private String encryption;
    @TableField("file_folder")
    private String fileFolder;

    private String bucket;

    @TableField("user_id")
    private String userId;


}
