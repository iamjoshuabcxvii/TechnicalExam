package com.job.technicalexam.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class MainView {

    @Autowired
    LoginView loginView;

    public void view() throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nPlease Enter key to start:");
        console.readLine();
        System.out.println("*************************");
        System.out.println("Theater Booking System");
        loginView.login();
//        showService.addShow();
//        customerService.view();
    }
}
