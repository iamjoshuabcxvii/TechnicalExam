package com.job.technicalexam.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeUtilTest {

    @InjectMocks
    DateTimeUtility dateTimeUtil;

    @Test
    public void getCurrentDateTime() {
        Timestamp mockedSavedDate = Timestamp.valueOf("2023-12-21 13:00:51.502");

        System.out.println("Minutes elapsed Since: " +
                dateTimeUtil.timeElapsedInMinutes(dateTimeUtil.currentDateTimeInEpoch(),
                        dateTimeUtil.epochOfTimestamp(mockedSavedDate)));
    }
}
