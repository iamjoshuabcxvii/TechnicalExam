package com.job.technicalexam.util;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public final class DateTimeUtility {

    public static long currentDateTimeInEpoch() {
        // Get the current instant
        Instant now = Instant.now();
        // Get the epoch in seconds
        long epochSeconds = now.getEpochSecond();
        // Print the epoch in seconds
//        System.out.println("Epoch in seconds: " + epochSeconds);

        return epochSeconds;
    }

    public static long epochOfTimestamp(Timestamp timestamp) {
        // Create a Timestamp object
        // Get the epoch in milliseconds
        long epochMillis = timestamp.getTime();
        // Create an Instant object from the epoch in milliseconds
        Instant instant = Instant.ofEpochMilli(epochMillis);
        // Get the epoch in seconds
        long epochSeconds = instant.getEpochSecond();
        // Print the epoch in seconds
//        System.out.println("Epoch in seconds: " + epochSeconds);

        return epochSeconds;
    }

    public static long timeElapsedInMinutes(long currentDateTime, long dateToCompareTo) {
        long difference = currentDateTime - dateToCompareTo;
        long minutesElapsed = difference / 60;
        return minutesElapsed;
    }
}
