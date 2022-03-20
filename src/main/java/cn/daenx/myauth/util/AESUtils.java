package cn.daenx.myauth.util;

import cn.daenx.myauth.base.exception.MyException;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES加解密工具
 *
 * @author DaenMax
 */
public class AESUtils {
    /**
     * AES加密，AES/ECB/PKCS5Padding
     *
     * @param sSrc 要加密的内容
     * @param sKey 密钥，16位，不支持中文
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey) {
        try {
            if (CheckUtils.isObjectEmpty(sSrc)) {
                return "";
            }
            if (sKey.length() != 16) {
                return "";
            }
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //算法/模式/补码方式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
            return new Base64().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new MyException("数据加密时发生异常");
        }
    }

    /**
     * AES解密，AES/ECB/PKCS5Padding
     *
     * @param sSrc 要解密的内容
     * @param sKey 密钥，16位，不支持中文
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey) {
        try {
            // 判断Key是否正确
            if (CheckUtils.isObjectEmpty(sKey)) {
                return "";
            }
            if (sKey.length() != 16) {
                return "";
            }
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //算法/模式/补码方式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, StandardCharsets.UTF_8);
            return originalString;
        } catch (Exception ex) {
            throw new MyException("数据解密时发生异常");
        }
    }
}
