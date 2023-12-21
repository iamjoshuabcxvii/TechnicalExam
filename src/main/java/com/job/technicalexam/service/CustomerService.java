package com.job.technicalexam.service;

import com.job.technicalexam.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CustomerService {
    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    @Autowired
    BookingsService bookingsService;

    @Autowired
    LoginService loginService;

    public void view() throws IOException {
        System.out.println("\n\n*************************");
        System.out.println("Customer Settings");
        System.out.println("1. Available Seats");
        System.out.println("2. Book Seats");
        System.out.println("3. Cancel Bookings");
        System.out.println("0. Log-out");
        System.out.println("Please Enter action to Take: ");
        int actionTaken = 0;
        try{
            actionTaken = Integer.parseInt(console.readLine());
        } catch (Exception exc) {
            invalidAction();
        }

        switch (actionTaken){
            case 0:
                System.out.println("Log out action selected");
                System.out.println("*************************");
                loginService.login();
                break;
            case 1:
                System.out.println("Available Tickets action selected: ");
                System.out.println("*************************");
                bookingsService.availableSeats();
                view();
                break;
            case 2:
                System.out.println("Book Tickets action selected: ");
                System.out.println("*************************");
                bookingsService.bookSeats();
                view();
                break;
            case 3:
                System.out.println("Cancel Tickets action selected: ");
                System.out.println("*************************");
                bookingsService.cancelBookedSeats();
                break;
            default:
                invalidAction();
                break;
        }
    }

    //Move into a utility
    private void invalidAction() throws IOException {
        System.out.println("Invalid action taken. Please try again. Press Enter key to continue.");
        console.readLine();
        view();
    }
}
