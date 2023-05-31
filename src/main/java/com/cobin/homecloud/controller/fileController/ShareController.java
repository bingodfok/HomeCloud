package com.cobin.homecloud.controller.fileController;


import com.cobin.homecloud.cloud_resource.minio.MinIOUtils;
import com.cobin.homecloud.common.annotation.Anonymous;
import com.cobin.homecloud.common.entity.SysFile;
import com.cobin.homecloud.common.security.SecurityContextPerceive;
import com.cobin.homecloud.common.vo.Result;
import com.cobin.homecloud.common.vo.SharePolicy;
import com.cobin.homecloud.services.SysFileService;
import com.cobin.homecloud.utils.IDUtils;
import com.cobin.homecloud.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 1_bit
 * @Date 2023/5/30 23:34
 */
@RestController
public class ShareController {

    private static final String SHARE_URL_CACHE_PREFIX = "share:";

    @Autowired
    SysFileService sysFileService;

    /**
     * 生成文件分享链接
     *
     * @param sharepolicy 分享策略
     */
    @GetMapping("/share/url")
    public Result getFileShareUrl(SharePolicy sharepolicy) {

        SysFile sysFile = sysFileService.getSysFileByUserAndId(SecurityContextPerceive.SecurityUser().getSysUser().getUserId(), sharepolicy.getFileId());
        if (sysFile != null) {
            //生成文件分享链接
            Map<String, Object> map = new HashMap<>();
            String shareID = IDUtils.generateID(8);
            if (sharepolicy.getMode() == 0) { //密码模式
                String absoluteUrl = MinIOUtils.getPresignedGetObjectUrl(sysFile.getFileName(), sysFile.getBucket(), 60 * 60 * 24 * 180); // 默认最长分享时间180天
                map.put("mode", 0);
                map.put("pass", sharepolicy.getSharePass());
                map.put("minio_url", absoluteUrl);
                RedisUtils.setMapValueTimeout(SHARE_URL_CACHE_PREFIX + shareID, map, 60 * 60 * 24 * 180L);
            } else if (sharepolicy.getMode() == 1) { //自动过期模式 并限制下载次数
                String absoluteUrl = MinIOUtils.getPresignedGetObjectUrl(sysFile.getFileName(), sysFile.getBucket(), sharepolicy.getExpiryTime());
                map.put("mode", 1);
                map.put("num", sharepolicy.getNum());
                map.put("minio_url", absoluteUrl);
                RedisUtils.setMapValueTimeout(SHARE_URL_CACHE_PREFIX + shareID, map, (long) sharepolicy.getExpiryTime());
            } else {
                return Result.error(2002, "请求参数错误");
            }
            return Result.success("http://192.167.23.5/share/" + shareID);
        } else {
            return Result.error(4001, "文件" + sharepolicy.getFileId() + "不存在");
        }
    }

    /**
     * 通过文件分享链接获取文件下载链接
     */
    @Anonymous
    @GetMapping("/share/download_url/{shareid}")
    public Result getFileDownloadUrl(@PathVariable("shareid") String shareid) {
        Map<Object, Object> mapValue = RedisUtils.getMapValue(SHARE_URL_CACHE_PREFIX + shareid);
        if(mapValue.isEmpty()){
           return Result.error(4002,"链接过期");
        }
        if((int)mapValue.get("mode")==1){
            if((int)mapValue.get("num")<=0){
                return Result.error(4003,"链接分享次数超限");
            }
            RedisUtils.subtractionValue(SHARE_URL_CACHE_PREFIX + shareid,"num",1);
        }
        String absoluteUrl = (String) mapValue.get("minio_url");
        return Result.success(absoluteUrl);
    }
}
