package com.cobin.homecloud.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 基于UUID的ID自动生成
 *
 * @Author 1_bit
 * @Date 2023/5/31 16:52
 */
public class IDUtils {
    private static final String BaseStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 利用UUID生成指定位数的ID 包含ALPHABE中随机的length个字母
     *
     * @param length ID长度
     * @return ID
     */
    public static String generateID(int length) {
        StringBuilder sb = new StringBuilder(length);
        List<Character> list = new ArrayList<>();
        //洗牌
        {
            char[] chars = BaseStr.toCharArray();
            for (char c : chars) {
                list.add(c);
            }
            Collections.shuffle(list);
        }
        //生成ID
        {
            UUID uuid = UUID.randomUUID();
            long bits = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits(); // 64位
            for (int i = 0; i < length; i++) {
                int idx = (int) (bits & 0x3D); // 0-61
                sb.append(list.get(idx));
                bits >>= 6;
            }
            return sb.toString();
        }

    }

}
