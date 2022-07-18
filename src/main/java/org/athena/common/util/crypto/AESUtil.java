package org.athena.common.util.crypto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AESUtil {

    private static final Integer KEY_SIZE = 128;

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"; //默认的加密算法

    private static final String SIGN_ALGORITHMS = "SHA1PRNG"; // 签名算法

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password),
                new IvParameterSpec(password.substring(0, 16).getBytes(StandardCharsets.UTF_8)));
        return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * AES 解密操作
     *
     * @param content  待解密内容
     * @param password 加密密码
     * @return 解密后的明文
     */
    public static String decrypt(String content, String password) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password),
                new IvParameterSpec(password.substring(0, 16).getBytes(StandardCharsets.UTF_8)));
        return new String(cipher.doFinal(Base64.getDecoder().decode(content)), StandardCharsets.UTF_8);
    }

    /**
     * 生成加密秘钥
     */
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance(SIGN_ALGORITHMS);
        random.setSeed(password.getBytes(StandardCharsets.UTF_8));
        //AES 要求密钥长度为 128
        generator.init(KEY_SIZE, random);
        //生成一个密钥
        SecretKey secretKey = generator.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }

}
