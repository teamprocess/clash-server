package com.process.clash.application.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    /**
     * 현재 일자를 yyyy-mm-dd 형식으로 반환받습니다.
     * @return String으로 현재 일자를 반환
     */
    public static String getCurrentDate() {
        LocalDate date = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        return date.format(formatter);
    }
}
