package com.process.clash.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    /**
     * Get the current date in yyyy-MM-dd format.
     * @return String current date
     */
    public static String getCurrentDate() {
        // Get the current date
        LocalDate date = LocalDate.now();

        // The ISO_LOCAL_DATE formatter defaults to the yyyy-MM-dd format.
        // Alternatively, you can use DateTimeFormatter.ofPattern("yyyy-MM-dd") for custom formats.
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        // Format the date to a string
        return date.format(formatter);
    }
}
