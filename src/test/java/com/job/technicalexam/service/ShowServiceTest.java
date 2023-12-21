package com.job.technicalexam.service;


import com.job.technicalexam.model.Bookings;
import com.job.technicalexam.repository.BookingsRepository;
import com.job.technicalexam.repository.ShowsListRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShowServiceTest {
    BufferedReader bufferedReader = mock(BufferedReader.class);

    @InjectMocks
    ShowService showService;

    @Mock
    BookingsRepository bookingsRepository;

    @Test
    public void testAddingOfShow() throws IOException {

        when(bufferedReader.readLine()).thenReturn("2", "10", "J", "");
        showService.addShow();
    }

    @Test
    public void testAddingInvalidShowRange() throws IOException {

        when(bufferedReader.readLine()).thenReturn("2", "27", "10", "K", "J");
        showService.addShow();
    }

    @Test
    public void testViewingShows() {
        when(bookingsRepository.findAll()).thenReturn(Collections.singletonList(mockOfExistingRecentlyBookedTickets()));
        showService.viewShowsAndBookings();
    }


    private Bookings mockOfExistingRecentlyBookedTickets() {
        Bookings bookings = new Bookings();

        bookings.setShowNumber(1);
        bookings.setUserId(1);
        bookings.setMobileNumber("09187654321");
        bookings.setSeatNumber("A1");
        bookings.setTicketNumber("BookingNo-1-1703068643");
        bookings.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));

        return bookings;
    }
}
