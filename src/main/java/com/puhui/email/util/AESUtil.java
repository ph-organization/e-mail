package com.puhui.email.util;

import com.puhui.email.entity.MailUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

/**
 * AES加解密工具
 *
 * @author Frank
 */
public class AESUtil {
    private static Logger logger = LoggerFactory.getLogger(AESUtil.class);
    private static final String KEY_ALGORITHM = "AES";
    private static final String CHARSET_NAME = "utf-8";//编码
    private static final String PASSWORD = "123.";//默认的密码

    /**
     * AES
     *
     * @param content 需要被加密的字符串
     * @return 密文
     */
    public static String encrypt(String content) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);// 创建AES的Key生产者
            kgen.init(128, new SecureRandom(PASSWORD.getBytes()));// 利用用户密码作为随机数初始化出
            // 128位的key生产者
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes(CHARSET_NAME);
            try {
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            byte[] result = cipher.doFinal(byteContent);// 加密
            return AESUtil.parseByte2HexStr(result);
        } catch (Exception e) {
            logger.error("AES字符串加密出现异常");
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param strResult AES加密过过的内容
     * @return 明文
     */
    public static String decrypt(String strResult) {
        try {
            byte[] content = AESUtil.parseHexStr2Byte(strResult);
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);// 创建AES的Key生产者
            kgen.init(128, new SecureRandom(PASSWORD.getBytes()));
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);
            return new String(result); // 明文
        } catch (Exception e) {
            logger.error("AES字符串解密出现异常");
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1){
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        int j=2;
        for (int i = 0; i < hexStr.length() / j; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 传入一个对象 ，给对象参数加密
     * 返回一个加密后的对象
     *
     * @param mailUser
     * @return
     */
    public static MailUser encryptUser(MailUser mailUser) {
        //给密码加密
        mailUser.setPwd(encrypt(mailUser.getPwd()));
        //电话加密
        mailUser.setPhone(encrypt(mailUser.getPhone()));
        return mailUser;
    }

    /**
     * 传入一个对象  给对象加密参数解密
     * 返回一个解密后的对象
     *
     * @param mailUser
     * @return
     */
    public static MailUser decryptUser(MailUser mailUser) {
        mailUser.setPwd(decrypt(mailUser.getPwd()));
        mailUser.setPhone(decrypt(mailUser.getPhone()));
        return mailUser;
    }
}
