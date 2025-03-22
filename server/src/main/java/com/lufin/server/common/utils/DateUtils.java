package com.lufin.server.common.utils;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import com.lufin.server.common.exception.BusinessException;

/**
 * 날짜 및 시간 관련 유틸리티 클래스
 * <p>
 * 날짜 포맷 변환, 현재 날짜/시간 가져오기, 날짜 간 차이 계산 등의 기능을 제공
 */
public class DateUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 현재 날짜 가져오기
    public static String getCurrentDate() {
        return LocalDate.now().format(DATE_FORMAT);
    }

    // 현재 날짜와 시간 가져오기
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMAT);
    }

    // 날짜와 문자열을 LocalDate로 변환
    public static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BusinessException(INVALID_INPUT_VALUE);
        }
    }

    // 두 날짜 문자열 사이의 일 수 차이 계산
    public static long daysBetween(String startDate, String endDate) {
        LocalDate start = parseDate(startDate);
        LocalDate end = parseDate(endDate);
        return ChronoUnit.DAYS.between(start, end);
    }
}
