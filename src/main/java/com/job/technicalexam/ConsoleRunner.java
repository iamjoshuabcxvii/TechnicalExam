package com.job.technicalexam;

import com.job.technicalexam.view.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ConsoleRunner implements CommandLineRunner {

    @Autowired
    MainView mainView;

    @Override
    public void run(String... args) throws Exception {
        main(args);
    }

    public void main(String[] args) throws IOException {
        mainView.view();
    }
}