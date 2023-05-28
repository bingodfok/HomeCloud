package com.cobin.homecloud.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.cobin.homecloud.common.exception.ServiceException;

import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA 非对称加密工具
 * 私钥固定 公钥每次使用随机生成
 *
 * @Author 1_bit
 * @Date 2023/5/5 12:23
 */
public class RsaUtil {
    private static final RSA RSA_IMPL;

    private static final PrivateKey PRIVATE_KEY;

    private static final PublicKey PUBLIC_KEY;

    /*
      RSA加解密对象
      确保内存中只有一个RSA_IMPL
     */
    static {
        KeyPair key = SecureUtil.generateKeyPair("RSA");
        PRIVATE_KEY = key.getPrivate();
        PUBLIC_KEY = key.getPublic();
        RSA_IMPL = new RSA("RSA", PRIVATE_KEY, PUBLIC_KEY);
    }

    /**
     * 获取加密私钥
     *
     * @return 私钥
     */
    public static PrivateKey getPrivateKey() {
        return RSA_IMPL.getPrivateKey();
    }

    /**
     * 获取加密公钥钥
     *
     * @return 获取公钥
     */
    public static PublicKey getPublicKey() {
        return RSA_IMPL.getPublicKey();
    }

    /**
     * Base64 字符串解密
     * 异常抛出 此处断言 解密异常来自密文错误 检查公钥和私钥是否匹配
     *
     * @param data    Base64 字符串
     * @param charset 结果编码
     * @return 结果
     */
    public static String decryStr(String data, Charset charset) {
        String result;
        try {
            result = RSA_IMPL.decryptStr(data, KeyType.PrivateKey, charset);
        } catch (Exception e) {
            throw new ServiceException(3001, "system.rsa.ciphertext_error", (Object) null);
        }
        return result;
    }

    public static String encrypt(String data, Charset charset) {
        return RSA_IMPL.encryptBase64(data, charset, KeyType.PublicKey);
    }
}
