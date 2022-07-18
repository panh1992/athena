package org.athena.common.util.crypto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

/**
 * CRC32计算
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CRC32Util {

    private static int[] ptiTable = new int[256];

    static {
        int data;
        int accum;
        for (int i = 0; i < 256; i++) {
            data = i << 24;
            accum = 0;
            for (int j = 0; j < 8; j++) {
                if (0 != ((data ^ accum) & 0x80000000)) {
                    accum = (accum << 1) ^ 0x04C11DB7;
                } else {
                    accum <<= 1;
                }
                data <<= 1;
            }
            ptiTable[i] = accum;
        }
    }

    /**
     * 将字符串转换为 CRC32 校验码
     */
    public static String calculate(String content) {
        return calculate(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将byte数组转换为 CRC32 校验码
     */
    public static String calculate(byte[] bytes) {
        int crc32 = 0xFFFFFFFF;
        if (null == bytes || bytes.length < 1) {
            return Integer.toHexString(crc32).toUpperCase();
        }
        for (byte data : bytes) {
            crc32 = (crc32 << 8) ^ ptiTable[(crc32 >>> 24) ^ (data & 0xFF)];
        }
        return Integer.toHexString(crc32).toUpperCase();
    }

}
