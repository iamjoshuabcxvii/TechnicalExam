package com.job.technicalexam.repository;

import com.job.technicalexam.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingsRepository extends JpaRepository<Bookings, Long> {

    Bookings findBookingsByShowNumberAndMobileNumberAndDeleted(int showNumber, String mobileNumber, boolean deleted);
    Bookings findBookingsByShowNumberAndSeatNumberAndDeleted(int showNumber, String seatNumber, boolean deleted);
    Bookings findBookingsByTicketNumberAndMobileNumberAndDeleted(String ticketNumber, String mobileNumber, boolean deleted);

}
