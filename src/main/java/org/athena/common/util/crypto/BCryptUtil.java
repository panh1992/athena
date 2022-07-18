package org.athena.common.util.crypto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

/**
 * 通用加密工具
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BCryptUtil {

    private static final String SALT = BCrypt.gensalt(12);

    /**
     * 使用 BCrypt 加密
     *
     * @param candidate 明文
     */
    public static String hashPassWord(String candidate) {

        return BCrypt.hashpw(candidate, SALT);

    }

    /**
     * 验证 BCrypt 密码正确性
     *
     * @param candidate 明文
     * @param hashed    密文
     */
    public static boolean checkPassWord(String candidate, String hashed) {

        return BCrypt.checkpw(candidate, hashed);

    }

}
