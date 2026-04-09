package xyz.playedu.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class VideoEncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY = "PlayEdu2023Secret";

    /**
     * 加密视频内容
     * @param content 视频内容
     * @return 加密后的内容
     * @throws Exception 加密异常
     */
    public static byte[] encrypt(byte[] content) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(content);
    }

    /**
     * 解密视频内容
     * @param encryptedContent 加密后的内容
     * @return 解密后的内容
     * @throws Exception 解密异常
     */
    public static byte[] decrypt(byte[] encryptedContent) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(encryptedContent);
    }

    /**
     * 加密字符串
     * @param content 字符串内容
     * @return 加密后的Base64字符串
     * @throws Exception 加密异常
     */
    public static String encryptString(String content) throws Exception {
        byte[] encrypted = encrypt(content.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 解密字符串
     * @param encryptedContent 加密后的Base64字符串
     * @return 解密后的字符串
     * @throws Exception 解密异常
     */
    public static String decryptString(String encryptedContent) throws Exception {
        byte[] encrypted = Base64.getDecoder().decode(encryptedContent);
        byte[] decrypted = decrypt(encrypted);
        return new String(decrypted);
    }
}
