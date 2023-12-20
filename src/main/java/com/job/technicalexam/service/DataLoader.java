package com.job.technicalexam.service;

import com.job.technicalexam.model.Users;
import com.job.technicalexam.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
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


    }
}
