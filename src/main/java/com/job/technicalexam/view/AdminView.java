package com.job.technicalexam.view;

import com.job.technicalexam.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class AdminView {

    @Autowired
    ShowService showService;

    @Autowired
    LoginView loginView;

    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    public void view() throws IOException {

        System.out.println("*************************");
        System.out.println("Administrator Settings");
        System.out.println("1. Setup");
        System.out.println("2. View");
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
                loginView.login();
            case 1:
                showService.addShow();
                view();
                break;
            case 2:
                showService.viewShowsAndBookings();
                view();
                break;
            default:
                invalidAction();
                break;
        }
    }
    private void invalidAction() throws IOException {
        System.out.println("Invalid action taken. Please try again. Press Enter key to continue.");
        console.readLine();
        view();
    }
}
