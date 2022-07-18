package org.athena.common.util.crypto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.athena.common.util.CommonUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA公钥/私钥/签名工具
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RSAUtil {

    //非对称密钥算法
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 2048;

    /**
     * 初始化密钥对
     */
    public static KeyPair initKeyPar() throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        // 生成密钥对
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 取得私钥
     *
     * @param keyPair 密钥对
     * @return 私钥
     */
    public static String getPrivateKey(KeyPair keyPair) {
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    /**
     * 根据私钥字符串 获取PrivateKey
     *
     * @param privateKey 私钥字符串
     */
    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(KEY_ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
    }

    /**
     * 取得公钥
     *
     * @param keyPair 密钥对
     * @return 公钥
     */
    public static String getPublicKey(KeyPair keyPair) {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    /**
     * 根据公钥字符串 获取PublicKey
     *
     * @param publicKey 公钥字符串
     */
    public static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(KEY_ALGORITHM)
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
    }

    /**
     * 公钥加密
     *
     * @param data      待加密数据
     * @param publicKey 密钥
     * @return 加密数据
     */
    public static String encryptByPublicKey(String data, String publicKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(x509KeySpec));
        return Base64.getEncoder().encodeToString(cipher.doFinal(CommonUtil.convertBytes(data)));
    }

    /**
     * 私钥解密
     *
     * @param data       待解密数据
     * @param privateKey 密钥
     * @return 解密数据
     */
    public static String decryptByPrivateKey(String data, String privateKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeySpecException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(pkcs8KeySpec));
        return new String(cipher.doFinal(Base64.getDecoder().decode(data)), StandardCharsets.UTF_8);
    }

    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 密钥
     * @return 加密数据
     */
    public static String encryptByPrivateKey(String data, String privateKey) throws InvalidKeySpecException,
            NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
            BadPaddingException, IllegalBlockSizeException {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePrivate(pkcs8KeySpec));
        return Base64.getEncoder().encodeToString(cipher.doFinal(CommonUtil.convertBytes(data)));
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 密钥
     * @return 解密数据
     */
    public static String decryptByPublicKey(String data, String publicKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeySpecException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException {

        //实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        //数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePublic(x509KeySpec));
        return new String(cipher.doFinal(Base64.getDecoder().decode(data)), StandardCharsets.UTF_8);
    }

}