package com.job.technicalexam;

import com.job.technicalexam.service.ShowService;
import com.job.technicalexam.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class MainView implements CommandLineRunner {
    @Autowired
    LoginService loginService;

    @Autowired
    ShowService showService;

    @Override
    public void run(String... args) throws Exception {
        main(args);
    }

    public void main(String[] args) throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nPlease Enter key to start:");
        console.readLine();
        System.out.println("*************************");
        System.out.println("Theater Booking System");
        loginService.login(); //actual process
//        showService.addShow();

    }
}