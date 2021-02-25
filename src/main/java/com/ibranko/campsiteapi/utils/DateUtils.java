package com.ibranko.campsiteapi.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateUtils {

    /**
     * Simulates the datesUntil method (JAVA 9).
     *
     * @param initDateInclusive the initial date, inclusive
     * @param endDateExclusive the end date, exclusive
     * @return a list containing a range of days between initDateInclusive and endDateExclusive
     */
    public static List<LocalDate> daysBetween(LocalDate initDateInclusive, LocalDate endDateExclusive) {
        List<LocalDate> daysBetween = Stream.iterate(initDateInclusive, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(initDateInclusive, endDateExclusive))
                .collect(Collectors.toList());

        return daysBetween;
    }
}
