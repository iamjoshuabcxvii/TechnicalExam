package com.job.technicalexam.service;

import com.job.technicalexam.model.Bookings;
import com.job.technicalexam.model.ShowsList;
import com.job.technicalexam.model.Users;
import com.job.technicalexam.repository.BookingsRepository;
import com.job.technicalexam.repository.ShowsListRepository;
import com.job.technicalexam.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ShowsListRepository showsListRepository;

    @Autowired
    private BookingsRepository bookingsRepository;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //Add Default Admin and Customers
        Users adminUser = new Users();
        adminUser.setUsername("admin1");
        adminUser.setPassword("pass1");
        adminUser.setRole("admin");

        Users userOne = new Users();
        userOne.setUsername("user1");
        userOne.setPassword("pass1");
        userOne.setRole("customer");


        Users userTwo = new Users();
        userTwo.setUsername("user2");
        userTwo.setPassword("pass2");
        userTwo.setRole("customer");


        usersRepository.save(adminUser);
        usersRepository.save(userOne);
        usersRepository.save(userTwo);

        //Add Default Shows
        ShowsList showOne = new ShowsList();
        showOne.setShowNumber(1);
        showOne.setRows(10);
        showOne.setColumns("J");

        showsListRepository.save(showOne);

        //Add Default Bookings
        Bookings bookingOne = new Bookings();
        bookingOne.setShowNumber(1);
        bookingOne.setMobileNumber("09187654321");
        bookingOne.setUserId(1);
        bookingOne.setTicketNumber("BookingNo-1A1-1703086400");
        bookingOne.setSeatNumber("A1");

        Bookings bookingTwo = new Bookings();
        bookingTwo.setShowNumber(1);
        bookingTwo.setMobileNumber("09187654321");
        bookingTwo.setUserId(1);
        bookingTwo.setTicketNumber("BookingNo-1A2-1703086400");
        bookingTwo.setSeatNumber("A2");

        Bookings bookingThree = new Bookings();
        bookingThree.setShowNumber(1);
        bookingThree.setMobileNumber("09187654321");
        bookingThree.setUserId(1);
        bookingThree.setTicketNumber("BookingNo-1A3-1703086400");
        bookingThree.setSeatNumber("A3");

        Bookings bookingFour = new Bookings();
        bookingFour.setShowNumber(1);
        bookingFour.setMobileNumber("09187654321");
        bookingFour.setUserId(1);
        bookingFour.setTicketNumber("BookingNo-1A4-1703086400");
        bookingFour.setSeatNumber("A4");

        Bookings bookingFive = new Bookings();
        bookingFive.setShowNumber(1);
        bookingFive.setMobileNumber("09187654321");
        bookingFive.setUserId(1);
        bookingFive.setTicketNumber("BookingNo-1A5-1703086400");
        bookingFive.setSeatNumber("A5");

        bookingsRepository.save(bookingOne);
        bookingsRepository.save(bookingTwo);
        bookingsRepository.save(bookingThree);
        bookingsRepository.save(bookingFour);
        bookingsRepository.save(bookingFive);

    }
}
