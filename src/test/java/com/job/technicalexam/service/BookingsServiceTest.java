package com.job.technicalexam.service;

import com.job.technicalexam.model.Bookings;
import com.job.technicalexam.model.ShowsList;
import com.job.technicalexam.repository.BookingsRepository;
import com.job.technicalexam.repository.ShowsListRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookingsServiceTest {

    @InjectMocks
    BookingsService bookingsService;

    @Mock
    ShowsListRepository showsListRepository;

    @Mock
    BookingsRepository bookingsRepository;

    BufferedReader bufferedReader = mock(BufferedReader.class);

    @Test
    public void successfulBooking() throws IOException {

        when(bufferedReader.readLine()).thenReturn("1", "09187654321", "A1", "0");
        when(showsListRepository.findShowsListByShowNumber(anyInt())).thenReturn(mockOfShows());
        when(bookingsRepository.findAllByShowNumberAndMobileNumberAndDeleted(anyInt(), anyString(), anyBoolean())).thenReturn(null);
        when(bookingsRepository.findAllByShowNumberAndMobileNumberAndDeleted(anyInt(), anyString(), anyBoolean()))
                .thenReturn(null);
        bookingsService.bookSeats();
    }

    @Test(expected = NullPointerException.class)
    public void testDuplicateBookingViaShowNumberAndMobileNumber() throws IOException {

        when(bufferedReader.readLine()).thenReturn("1", "09187654321", "A1", "");
        when(showsListRepository.findShowsListByShowNumber(anyInt())).thenReturn(mockOfShows());
        when(bookingsRepository.findAllByShowNumberAndMobileNumberAndDeleted(anyInt(), anyString(), anyBoolean())).thenReturn(mockOfExistingBookings());
        bookingsService.bookSeats();
    }

    @Test
    public void testDuplicateBookingViaShowNumberAndSeatNumber() throws IOException {

        when(bufferedReader.readLine()).thenReturn("1", "09187654321", "A1", "");
        when(showsListRepository.findShowsListByShowNumber(anyInt())).thenReturn(mockOfShows());
        when(bookingsRepository.findAllByShowNumberAndMobileNumberAndDeleted(anyInt(), anyString(), anyBoolean())).thenReturn(null);
        when(bookingsRepository.findBookingsByShowNumberAndSeatNumberAndDeleted(anyInt(), anyString(), anyBoolean()))
                .thenReturn(mockOfExistingBookings());
        bookingsService.bookSeats();
    }

    @Test(expected = NullPointerException.class)
    public void invalidShowInputtedTest() throws IOException {

        when(bufferedReader.readLine()).thenReturn("2", "");
        when(showsListRepository.findShowsListByShowNumber(anyInt())).thenReturn(null);
        bookingsService.bookSeats();
    }

    @Test
    public void outOfRangeSeatsTest() throws IOException {
        when(bufferedReader.readLine()).thenReturn("1", "09187654321", "B1");
        when(showsListRepository.findShowsListByShowNumber(anyInt())).thenReturn(mockOfShows());
        when(bookingsRepository.findAllByShowNumberAndMobileNumberAndDeleted(anyInt(), anyString(), anyBoolean())).thenReturn(null);
        when(bookingsRepository.findAllByShowNumberAndMobileNumberAndDeleted(anyInt(), anyString(), anyBoolean()))
                .thenReturn(null);
        bookingsService.bookSeats();
    }

    private ShowsList mockOfShows() {
        ShowsList showsList = new ShowsList();
        showsList.setShowNumber(1);
        showsList.setRows(1);
        showsList.setColumns("A");
        showsList.setCancellationTimeFrame(2);
        return showsList;
    }

    private Bookings mockOfExistingBookings() {
        Bookings bookings = new Bookings();

        bookings.setShowNumber(1);
        bookings.setUserId(1);
        bookings.setMobileNumber("09187654321");
        bookings.setSeatNumber("A1");
        bookings.setTicketNumber("BookingNo-1-1703068643");

        return bookings;
    }

}
