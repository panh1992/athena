package org.athena.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QueryUtil {

    public static Long limit(Long limit) {
        return Objects.isNull(limit) || limit < 0 ? Long.valueOf(20) : limit;
    }

    public static Long offset(Long offset) {
        return Objects.isNull(offset) || offset < 0 ? Long.valueOf(0) : offset;
    }

    public static String like(String search) {
        return "%".concat(search).concat("%");
    }

    /**
     * 判断是否进行分页处理
     *
     * @param page 分页页码
     * @param size 每页数量
     * @return true:是  false:否
     */
    public static boolean isPage(Integer page, Integer size) {
        return Objects.isNull(page) || Objects.isNull(size);
    }

}
