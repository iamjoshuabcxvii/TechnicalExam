package com.job.technicalexam.service;

import com.job.technicalexam.model.Bookings;
import com.job.technicalexam.model.ShowsList;
import com.job.technicalexam.repository.BookingsRepository;
import com.job.technicalexam.repository.ShowsListRepository;
import com.job.technicalexam.util.DateTimeUtility;
import com.job.technicalexam.view.CustomerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class BookingsService {
    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    final String BOOKING_NO_PREFIX = "BookingNo-";

    @Autowired
    CustomerView customerView;

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
            customerView.view();
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
        customerView.view();
    }

    private String enterPhoneNumber(int showNumber) throws IOException {
        String phoneNumber;
        System.out.print("Enter phone number: ");
        phoneNumber = console.readLine();

        validateBookingViaShowNumberAndPhoneNumber(showNumber, phoneNumber);

        return phoneNumber;
    }

    private void validateBookingViaShowNumberAndPhoneNumber(int showNumber, String phoneNumber) throws IOException {
        Bookings bookings = bookingsRepository.findDistinctFirstByShowNumberAndMobileNumberAndDeleted(showNumber, phoneNumber, false);

        if (Optional.ofNullable(bookings).isPresent()) {
            bookingAlreadyExistAction();
        }
    }

    private void bookingAlreadyExistAction() throws IOException {
        System.out.print("Booking already exist. Please try again with a different mobile number. Press Enter key to continue.");
        console.readLine();
        customerView.view();
    }

    private void bookedSeats(int showNumber, String phoneNumber) throws IOException {
        String seatsBooked;
        List<String> listOfSeats;
        System.out.print("Enter each comma separate seat numbers to book: ");
        seatsBooked = console.readLine();
        listOfSeats = Arrays.asList(separateIntoListOfSeats(seatsBooked.toUpperCase(Locale.ROOT)));
        Set<String> setWithoutDuplicates = new LinkedHashSet<>(listOfSeats);
        List<String> listOfInputtedSeatsWithoutDuplicates = new ArrayList<>(setWithoutDuplicates);

        listOfInputtedSeatsWithoutDuplicates.stream().forEach(seat ->
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
        bookings = bookingsRepository.findBookingsByShowNumberAndSeatNumberAndDeleted(showNumber, seatNumber, false);
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
        boolean isInvalidInput = false;
        int inputtedColumnInt = 0;
        int inputtedRow = 0;
        int minimumColumnInt = 0;
        int maximumColumnInt = 0;

        if (!seatNumber.matches("([a-jA-J]\\d{1,2})")) {
            isInvalidInput = true;
            invalidSeatError(seatNumber);
        } else {
            String inputtedColumnStr = seatNumber.substring(0, 1);
            char inputtedColumnn = inputtedColumnStr.toUpperCase().charAt(0);
            inputtedColumnInt = Character.valueOf(inputtedColumnn).charValue();

            inputtedRow = Integer.parseInt(seatNumber.substring(1));

            char minimumColumn = 'A';
            minimumColumnInt = Character.valueOf(minimumColumn).charValue();
            char maximumColumn = showsList.getColumns().charAt(0);
            maximumColumnInt = Character.valueOf(maximumColumn).charValue();

        }

        if (inputtedColumnInt < minimumColumnInt || inputtedColumnInt > maximumColumnInt
                || inputtedRow > showsList.getRows()
        ) {
            invalidSeatError(seatNumber);
            isInvalidInput = true;
        }

        return isInvalidInput;
    }

    private void invalidSeatError(String seatNumber) {
        System.out.println("Seat Number " + seatNumber + " is invalid for the show as it is out of Range. Please try again.");
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
                    Bookings bookings = new Bookings();
                    bookings.setShowNumber(showNumber);
                    bookings.setMobileNumber(mobileNumber);
                    bookings.setSeatNumber(seat);
                    bookings.setTicketNumber(generateTicketNumber(showNumber, seat));
                    bookingsRepository.save(bookings);
                }
        );
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

    public void cancelBookedSeats() throws IOException {
        String ticketNumber = null, mobileNumber = null;

        System.out.print("Enter Ticket Number: ");
        ticketNumber = console.readLine();
        System.out.print("Enter Mobile Number: ");
        mobileNumber = console.readLine();

        Optional<Bookings> bookingsResult = Optional.ofNullable(bookingsRepository.findBookingsByTicketNumberAndMobileNumberAndDeleted(ticketNumber, mobileNumber, false));

        if (bookingsResult.isPresent()) {
            ShowsList showsList = showsListRepository.findShowsListByShowNumber(bookingsResult.get().getShowNumber());
            long elapsedMinute = DateTimeUtility.timeElapsedInMinutes(DateTimeUtility.currentDateTimeInEpoch(),
                    DateTimeUtility.epochOfTimestamp(bookingsResult.get().getCreatedDate()));
            if (elapsedMinute > showsList.getCancellationTimeFrame()) {
                System.out.println("Booking Cancellation failed as " + elapsedMinute + " minutes has already elapsed. Bookings that were booked since " +
                        showsList.getCancellationTimeFrame() + " minutes has elapsed could no longer be cancelled.");
            } else {
                //Continue with Cancellation
                bookingsResult.get().setDeleted(true);
                bookingsRepository.save(bookingsResult.get());
                System.out.println("Successfully canceled Booking with Ticket No.: " + ticketNumber);
                customerView.view();
            }
        } else {
            System.out.println("Ticket Number or Mobile Number booked not found. Please try again. Press Enter to continue.");
            console.readLine();
            customerView.view();
        }

    }

    public void availableSeats() throws IOException {
        // View all active bookings for a show
        int showNumber = 0;
        System.out.print("Enter Show Number: ");
        try {
            showNumber = Integer.parseInt(console.readLine());
        } catch (Exception exc) {
            invalidShowAction();
        }
        List<Bookings> allBookedSeats = bookingsRepository.findAllByShowNumberAndDeleted(showNumber, false);

        List<String> listOfAllBookedSeats = new ArrayList<>();

        allBookedSeats.stream().forEach(record -> {
            listOfAllBookedSeats.add(record.getSeatNumber());
        });

        // View all bookable/enabled seats
        Optional<ShowsList> showsList = Optional.ofNullable(showsListRepository.findShowsListByShowNumber(showNumber));
        if (showsList.isPresent()) {
            Optional<List<String>> listOfAllBookableSeats;
            listOfAllBookableSeats = Optional.ofNullable(allBookableSeats(showsList.get().getColumns(), showsList.get().getRows()));

            //Determine what elements on both list are duplicates
            List<String> duplicates = new ArrayList<>(listOfAllBookedSeats);
            duplicates.retainAll(listOfAllBookableSeats.get());

            //Retain only vacant seats
            List<String> unique2 = listOfAllBookableSeats.get();
            unique2.removeAll(duplicates);

            System.out.println("Vacant Seats:");
            unique2.stream().forEach(record -> {
                        System.out.print(record + " ");
                    }
            );
            System.out.println("\n\nPress Enter key to continue: ");
            console.readLine();
        } else {
            invalidShowAction();
        }
    }


    private List<String> allBookableSeats(String column, int maximumRow) {
        String inputtedColumnStr = column.substring(0, 1);
        char inputtedColumnn = inputtedColumnStr.toUpperCase().charAt(0);
        int maximumColumnInt = Character.valueOf(inputtedColumnn).charValue();
        int minimumColumnInt = 65;
        int minimumRow = 1;

        List<String> bookableSeats = new ArrayList<>();

        for (int ctr1 = minimumColumnInt; ctr1 <= maximumColumnInt; ctr1++) {

            for (int ctr2 = minimumRow; ctr2 <= maximumRow; ctr2++) {
                bookableSeats.add(String.valueOf(Character.toChars(ctr1)) + ctr2);
            }

        }
//        bookableSeats.stream().forEach(seat -> {
//            System.out.println("Bookable Seats:"+seat);
//        });
        return bookableSeats;

    }
}

