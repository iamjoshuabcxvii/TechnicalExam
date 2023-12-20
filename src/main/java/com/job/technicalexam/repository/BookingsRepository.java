package com.job.technicalexam.repository;

import com.job.technicalexam.model.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingsRepository extends JpaRepository<Bookings, Long> {

    Bookings findBookingsByShowNumberAndMobileNumber(int showNumber, String mobileNumber);
    Bookings findBookingsByShowNumberAndSeatNumber(int showNumber, String seatNumber);

}
