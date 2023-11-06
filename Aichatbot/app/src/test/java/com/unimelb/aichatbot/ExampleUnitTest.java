package com.unimelb.aichatbot;

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {


        String dateStr = "2023-11-05T23:26:10.259870";
        Date date = parse(dateStr);
        System.out.println("Parsed Date: " + date);


    }

    public static Date parse(String dateStr) {
        // Define the formatter that parses the ISO 8601 string.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        // Parse the LocalDateTime using the formatter.
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);

        // Convert LocalDateTime to Instant specifying the ZoneId as UTC.
        Instant instant = dateTime.atZone(ZoneId.of("UTC")).toInstant();

        // Convert Instant to Date.
        return Date.from(instant);
    }

}