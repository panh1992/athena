package org.athena.auth.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.athena.common.util.crypto.RSAUtil;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * jwt 工具类
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JWTUtil {

    /**
     * 生成一个RSA密钥对，用于JWT的签名和验证
     */
    private static RSAPrivateKey rsaPrivateKey;

    private static RSAPublicKey rsaPublicKey;

    private static final String PRIVATE_KEY_ID = "岁月无声";

    private static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCq1QDG/H4bwSK/8lF1"
            + "pe0aTovIhdsVaMeQ7NfHvwzen6LPQdG09rqK6mvxZpNqRLpOQFOdXIR2bUgbbyWHAjlWb/AUaFd4Qi0BknJHxeknJG5cmEy4XgLd"
            + "lI3+G+hifw6OaXeJPhGRXts2frZSh2puyiUNNOxDzxyHtxC2IP+M5ajDGTP6U5JX1TK8sdXa5ce+ncptRHmJd/pRqeVIuUG8biBH"
            + "N5KjBF50SQhgAn0dXTvByIIHZne+6SgnHDx4DYEyD7tDBFQ7Vw2txi1MbpSMe/H3Ibwn7yVKWTZ3oj1meW0qDOp7L2iAwekUH+AR"
            + "6hKvjHpJI4+ppjkbqaxWZyp3AgMBAAECggEAHwOQoo+QbYWAw4xAkbVP+GiuI/d5MMfXA06cVh0vPvOIYUw3wDzS3Ql2E1tmA/L6"
            + "R96Rz43OVpm8b46cJ4iV1EV6NppC1whcl1549OLPU1Xf21ujAg2/Q28G4E08wTUD9YLcOsRT+CpabehPPWhsQDTeySkEg8MEEPeB"
            + "Ry9ydaemojCTj8kB27XdzDdleZI1njdvJBH17e+PDkKVI2Ra7v4kG+KuTfs46wp571SExXOxC2hmluwso8z5PIDh4BpmS7A/KPSw"
            + "pm6m+4rzLXCrYNGUQgOaEbVMoE2OpF5Z3AwEzIN/sylrwpNqlH9Mj0ec7uO2DFExADbEgEVCrDUDQQKBgQD/3h4Sn7VYLj8LPjr0"
            + "sKNjiFMBk+ECz/zOlfkwBZYgvhBApTUn25Z3kYlI5lcYtxfFydGU9qCgKV9IngAPU/9fFm5BVWNz/1El+R5Zj60jtYB1udvOO7d7"
            + "ZOnv505dM/JT/BFZWW1hul5L9KehVyGSc3+iskG0/g5O8f63tSC3pwKBgQCq65/+ME4Qv/KkPkb9t8G97PE0cOHOmVjPQqXVc3k6"
            + "073P7Ol0RmKbqMwey9oJg/8tm3/w/vvmeVFOZf43tKNneW4IxSNb16+5RPzjAWVKbD3Gb5/HgnUQUm6RGPow9fDPlSsG8MNML7UM"
            + "nzFOCWZW3pzlA9BPxjxIgrUBP/xQsQKBgQCZxQlZs/WuKwzkWt/fkhB/jrwj5JWDtS9/kA4T2CejQdZOo3rQm11QOnzA/P0280Is"
            + "Md/soFNkLbj/0UTD8X01ziswxHpiifxgz9h3hqtNHJU5Kxt4U9cvJzSYYvBrfv6Rjpl2kxHze6eUClJ72ftpIlSAmJR4i35Z34Fv"
            + "MkGZXQKBgCHODIzXvPjvHeyCLMRNrdIEpQg8M00LY1dK1UXMWvPZoTcYomvO5/3lesm0g+FR7Ax3LuzZYbUJ1Zzt60PVRGRYzfyK"
            + "ER4/IJBCJso3InN/yRAQT8fD86dQxnGIQSOh4QkNdb8fq2DMJsDiU3wdu9H+oYViJbdBe/bTcz1WDG9hAoGAYbVu69bus9Ew2LA3"
            + "Jd2eDwQDfRXmhjn1taiXlLPDNxx3CazHcS2glOunsPfKalR26Fvf3yauc2AmByeG+5AwzPvsEWBPWS7H9e8UJWycDUyRIzKbWpYp"
            + "4/d1IW6Q1yu9y67zbWi57cR8TCsXU9qPxJcytoDfsGSmbjy/uNqnbMc=";

    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqtUAxvx+G8Eiv/JRdaXtGk6Ly"
            + "IXbFWjHkOzXx78M3p+iz0HRtPa6iupr8WaTakS6TkBTnVyEdm1IG28lhwI5Vm/wFGhXeEItAZJyR8XpJyRuXJhMuF4C3ZSN/hvoY"
            + "n8Ojml3iT4RkV7bNn62UodqbsolDTTsQ88ch7cQtiD/jOWowxkz+lOSV9UyvLHV2uXHvp3KbUR5iXf6UanlSLlBvG4gRzeSowRed"
            + "EkIYAJ9HV07wciCB2Z3vukoJxw8eA2BMg+7QwRUO1cNrcYtTG6UjHvx9yG8J+8lSlk2d6I9ZnltKgzqey9ogMHpFB/gEeoSr4x6S"
            + "SOPqaY5G6msVmcqdwIDAQAB";

    private static final String ISSUER = "athena";

    private static final String AUDIENCE = "web";

    static {
        try {
            rsaPrivateKey = (RSAPrivateKey) RSAUtil.getPrivateKey(PRIVATE_KEY);
            rsaPublicKey = (RSAPublicKey) RSAUtil.getPublicKey(PUBLIC_KEY);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("获取 JWT 公钥 私钥 异常: {}", e.getMessage());
        }
    }

    /**
     * 创建 jwt token
     *
     * @param subject 令牌主题
     * @param minutes 有效分钟数
     */
    public static String createToken(String subject, long minutes) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(ISSUER);  // 令牌的创建者
        claims.setAudience(AUDIENCE); // 要将令牌发送给谁
        claims.setExpirationTimeMinutesInTheFuture(minutes); // 令牌到期的时间 从现在起 分钟数
        claims.setGeneratedJwtId(); // 用于令牌的唯一标识符
        claims.setIssuedAtToNow();  // 发出/创建令牌时（现在）
        claims.setNotBeforeMinutesInThePast(2); // 令牌尚未生效的时间（2分钟前）
        claims.setSubject(subject); // 主题/委托人是令牌的对象

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaPrivateKey);
        jws.setKeyIdHeaderValue(PRIVATE_KEY_ID);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);
        return jws.getCompactSerialization();
    }

    /**
     * 验证 jwt token 的有效性
     */
    public static JwtClaims validation(String jwtToken, int minutes) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // JWT必须有一个到期时间
                .setMaxFutureValidityInMinutes(minutes) // 但到期时间不能太疯狂
                .setAllowedClockSkewInSeconds(30) // 允许一些余地来验证基于时间的索赔，以解决时钟偏差问题
                .setRequireSubject() // 必须有一个主题声明
                .setExpectedIssuer(ISSUER) // 需要由谁发出
                .setExpectedAudience(AUDIENCE) // 目标对象
                .setVerificationKey(rsaPublicKey) // 使用公钥验证签名
                .build();

        //  验证JWT并将其处理为声明
        JwtClaims jwtClaims = jwtConsumer.processToClaims(jwtToken);
        log.debug("JWT validation succeeded! {}", jwtClaims);
        return jwtClaims;
    }

}
