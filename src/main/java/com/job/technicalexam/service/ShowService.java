package com.job.technicalexam.service;

import com.job.technicalexam.model.Bookings;
import com.job.technicalexam.model.ShowsList;
import com.job.technicalexam.repository.BookingsRepository;
import com.job.technicalexam.repository.ShowsListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ShowService {
    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    final int MAX_ROWS = 26;
    final int MAX_COLUMNS = 10;
    @Autowired
    ShowsListRepository showsListRepository;

    @Autowired
    BookingsRepository bookingsRepository;


    public void addShow() throws IOException {
        int showNumber = 0, rows = 0, cancellationWindow = 0;
        String columns = null;


        try {
            showNumber = addShowNumber();
            rows = addNumberOfRows();
            columns = addNumberOfColumns();
            cancellationWindow = addCancellationTimeWindow();
        } catch (Exception exc) {
            invalidAction();
        }
        addShowInDb(showNumber, rows, columns, cancellationWindow);
    }

    private void addShowInDb(int showNumber, int rows, String columns, int cancellationTime) {
        ShowsList showsList = new ShowsList();

        showsList.setShowNumber(showNumber);
        showsList.setRows(rows);
        showsList.setColumns(columns);
        showsList.setCancellationTimeFrame(cancellationTime);
        showsListRepository.save(showsList);

    }

    private int addShowNumber() throws IOException {
        int showNumber;
        System.out.print("Add Show Number: ");
        showNumber = Integer.parseInt(console.readLine());

        return showNumber;
    }

    private int addNumberOfRows() throws IOException {
        int rows;
        System.out.print("Number of Rows (1 to 26): ");
        rows = Integer.parseInt(console.readLine());
        if (rows > MAX_ROWS) {
            System.out.println("Invalid number of rows. Maximum number of rows allowed is " + MAX_ROWS + " only. Please try again");
            addNumberOfRows();
        }

        return rows;
    }

    private String addNumberOfColumns() throws IOException {
        String columns;
        System.out.print("Number of Columns (A to J): ");
        columns = console.readLine();

        if (!columns.toUpperCase().matches("[A-J]")) {
            System.out.println("Invalid number of columns. Maximum number of rows allowed is " + MAX_COLUMNS + " only. Please try again");
            addNumberOfColumns();
        }
        return columns.toUpperCase();
    }

    private int addCancellationTimeWindow() throws IOException {
        int cancellationWindow;
        System.out.print("Cancellation Window (in minutes): ");
        cancellationWindow = Integer.parseInt(console.readLine());

        return cancellationWindow;
    }

    private void invalidAction() throws IOException {
        System.out.println("Invalid action taken. Please try again. Press Enter key to continue.");
        console.readLine();
        addShow();
    }

    public void viewShowsAndBookings() {
        System.out.println("List of Bookings");
        List<Bookings> bookingsList = bookingsRepository.findAll();

        bookingsList.stream().forEach(record -> {
            System.out.println("--> " + record);
        });
    }
}
