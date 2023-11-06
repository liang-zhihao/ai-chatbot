package com.unimelb.aichatbot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateParser {
    public static Date parse(String dateStr) {
        try {
            // Parse the string to an OffsetDateTime
            OffsetDateTime odt = OffsetDateTime.parse(dateStr);

            // Convert OffsetDateTime to an Instant and then to a java.util.Date
            return Date.from(odt.toInstant());
        } catch (Exception e) {
            // Wrap any parsing exception into ParseException for consistency
            e.printStackTrace();
        }
        return null;
    }

}
