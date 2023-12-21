package com.job.technicalexam.view;

import com.job.technicalexam.TechnicalExamApplication;
import com.job.technicalexam.model.Users;
import com.job.technicalexam.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@Service
public class LoginView extends TechnicalExamApplication {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    AdminView adminView;

    @Autowired
    CustomerView customerView;

    public void login() throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("\nEnter Username: ");
        String username = console.readLine();
        System.out.print("\nEnter Password: ");
        String password = console.readLine();

//        System.out.println("Your Username is:"+username);
//        System.out.println("Your Password is:"+password);
        validate(username, password);
    }

    private void validate(String username, String password) throws IOException {

        Users userDetails = usersRepository.findByUsername(username);

        if (Optional.ofNullable(userDetails).isPresent()) {
            if (userDetails.getPassword().equals(password)) {
                validateRole(userDetails);
            } else {
                invalidPasswordAction();
            }
        } else {
            invalidPasswordAction();
        }
    }

    private void validateRole(Users userDetails) throws IOException {
        if (userDetails.getRole().equals("admin")) {
            adminView.view();
        } else {
            customerView.view();
        }
    }

    private void invalidPasswordAction() throws IOException {
        System.out.println("Invalid Username or password. Please try again.");
        login();
    }
}
