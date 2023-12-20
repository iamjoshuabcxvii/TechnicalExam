package com.job.technicalexam.service;

import com.job.technicalexam.model.Bookings;
import com.job.technicalexam.model.ShowsList;
import com.job.technicalexam.repository.BookingsRepository;
import com.job.technicalexam.repository.ShowsListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class BookingsService {
    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    final String BOOKING_NO_PREFIX = "BookingNo-";

    @Autowired
    CustomerService customerService;

    @Autowired
    ShowsListRepository showsListRepository;

    @Autowired
    BookingsRepository bookingsRepository;


    boolean isDuplicateExist = false, outOfConfiguredRange = false;

    public void bookSeats() throws IOException {

        int showNumber;
        String phoneNumber;
        showNumber = bookShowNumber();
        phoneNumber = enterPhoneNumber(showNumber);
        bookedSeats(showNumber, phoneNumber);

    }

    private int bookShowNumber() throws IOException {
        int showNumber = 0;
        System.out.print("Enter Show number to book: ");
        try {
            showNumber = Integer.parseInt(console.readLine());
        } catch (Exception exc) {
            invalidShowAction();
            customerService.view();
        }
        checkIfExist(showNumber);

        return showNumber;
    }

    private void checkIfExist(final int showNumber) throws IOException {
        ShowsList showsListResult;
        showsListResult = showsListRepository.findShowsListByShowNumber(showNumber);
        if (!Optional.ofNullable(showsListResult).isPresent()) {
            invalidShowAction();
        }
    }

    private void invalidShowAction() throws IOException {
        System.out.println("Invalid Show number inputted. Please try again. Press Enter key to continue.");
        console.readLine();
        customerService.view();
    }

    private String enterPhoneNumber(int showNumber) throws IOException {
        String phoneNumber;
        System.out.print("Enter phone number: ");
        phoneNumber = console.readLine();

        validateBookingViaShowNumberAndPhoneNumber(showNumber, phoneNumber);

        return phoneNumber;
    }

    private void validateBookingViaShowNumberAndPhoneNumber(int showNumber, String phoneNumber) throws IOException {
        Bookings bookings;
        bookings = bookingsRepository.findBookingsByShowNumberAndMobileNumber(showNumber, phoneNumber);

        if (Optional.ofNullable(bookings).isPresent()) {
            bookingAlreadyExistAction();
        }
    }

    private void bookingAlreadyExistAction() throws IOException {
        System.out.print("Booking already exist. Please try again with a different mobile number. Press Enter key to continue.");
        console.readLine();
        customerService.view();
    }

    private void bookedSeats(int showNumber, String phoneNumber) throws IOException {
        String seatsBooked;
        String ticketNumber = null;
        List<String> listOfSeats;
        System.out.print("Enter each comma separate seat numbers to book: ");
        seatsBooked = console.readLine();
        listOfSeats = Arrays.asList(separateIntoListOfSeats(seatsBooked.toUpperCase(Locale.ROOT)));

        listOfSeats.stream().forEach(seat ->
                {
                    try {
                        validateBookingViaShowNumberAndSeatNumber(showNumber, seat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        if (!isDuplicateExist && !outOfConfiguredRange) {
            saveBookingInDb(showNumber, phoneNumber, listOfSeats);
        }

    }

    private boolean validateBookingViaShowNumberAndSeatNumber(int showNumber, String seatNumber) throws IOException {
        ShowsList showsList;
        boolean result = false;
        showsList = showsListRepository.findShowsListByShowNumber(showNumber);
        if (Optional.ofNullable(showsList).isPresent()) {
            outOfConfiguredRange = vaidateIfWithinRangeOfColumns(seatNumber, showsList);
        }

        Bookings bookings;
        bookings = bookingsRepository.findBookingsByShowNumberAndSeatNumber(showNumber, seatNumber);
        if (Optional.ofNullable(bookings).isPresent()) {
            System.out.println("Duplicate bookings were found for Seat Number: " + seatNumber);
            isDuplicateExist = true;
            System.out.println("Press Enter key to continue.");
            console.readLine();
        }
        if (isDuplicateExist && outOfConfiguredRange) {
            result = true;
        }

        return result;
    }

    private boolean vaidateIfWithinRangeOfColumns(String seatNumber, ShowsList showsList) {
        boolean result = false;
        String inputtedColumnStr = seatNumber.substring(0, 1);
        char inputtedColumnn = inputtedColumnStr.toUpperCase().charAt(0);
        int inputtedColumnInt = Character.valueOf(inputtedColumnn).charValue();

        int inputtedRow = Integer.parseInt(seatNumber.substring(1));

        char minimumColumn = 'A';
        int minimumColumnInt = Character.valueOf(minimumColumn).charValue();
        char maximumColumn = showsList.getColumns().charAt(0);
        int maximumColumnInt = Character.valueOf(maximumColumn).charValue();

        if (inputtedColumnInt < minimumColumnInt || inputtedColumnInt > maximumColumnInt
                || inputtedRow > showsList.getRows()
        ) {
            System.out.println("Seat Number " + seatNumber + " is invalid for the show as it is out of Range. Please try again.");
            result = true;
        }


        return result;
    }

    private String[] separateIntoListOfSeats(String seatsBooked) {
        String[] listOfBookedSeats = sanitizeInput(seatsBooked).split(",");

        return listOfBookedSeats;
    }

    private String sanitizeInput(String input) {
        String sanitizedInput = input.replaceAll(" ", "");
        return sanitizedInput;
    }


    @Transactional
    void saveBookingInDb(int showNumber, String mobileNumber, List<String> bookedSeats) {

        bookedSeats.stream().forEach(seat -> {
//                    try {
                    Bookings bookings = new Bookings();
                    bookings.setShowNumber(showNumber);
                    bookings.setMobileNumber(mobileNumber);
                    bookings.setSeatNumber(seat);
                    bookings.setTicketNumber(generateTicketNumber(showNumber, seat));
                    bookingsRepository.save(bookings);
//                    } catch (Exception exc) {
//                        errorWhileSaving();
//                        try {
//                            console.readLine();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }

                }

        );
//        System.out.println("Successfully Generated Booking with Reference No.: "+ticketNumber);
    }

    private void errorWhileSaving() {
        System.out.println("An error was encountered while saving some of the records. " +
                "Rolling back changes. Please try again. Press enter key to continue.");
    }

    private String generateTicketNumber(int showNumber, String seatNumber) {
        String bookingNumber;
        long epoch = System.currentTimeMillis() / 1000;

        bookingNumber = BOOKING_NO_PREFIX + showNumber + seatNumber + "-" + epoch;

        System.out.println("Successfully Generated Booking with Reference No.: " + bookingNumber);
        return bookingNumber;
    }


    public void availableSeats() {

    }

    public void cancelBookedSeats() {

    }
}
