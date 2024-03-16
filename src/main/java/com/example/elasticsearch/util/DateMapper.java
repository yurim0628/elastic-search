package com.example.elasticsearch.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.elasticsearch.constant.ElasticsearchConstants.DATE_TIME_PATTERN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateMapper {

    public static String formatLocalDate(LocalDate localDate) {
        return localDate.atStartOfDay().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }
}

