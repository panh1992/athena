package org.athena.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(Constant.DATE_TIME_FORMAT);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT);

    /**
     * 将时间字符串转换成LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
    }

    /**
     * 将LocalDateTime格式化成时间字符串
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * 将时间字符串转换成LocalDate
     */
    public static LocalDate parseLocalDate(String dateTimeStr) {
        return LocalDate.parse(dateTimeStr, DATE_FORMATTER);
    }

    /**
     * 将LocalDate格式化成时间字符串
     */
    public static String formatLocalDate(LocalDate localDate) {
        return localDate.format(DATE_FORMATTER);
    }

    /**
     * 判断时间是否在有效期内
     *
     * @param currentTime 验证时间
     * @param startTime   有效期开始时间
     * @param endTime     有效期结束时间
     */
    public static boolean validityPeriod(LocalDateTime currentTime, LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.isBefore(currentTime) && currentTime.isBefore(endTime);
    }

}
