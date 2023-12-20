package com.job.technicalexam.repository;

import com.job.technicalexam.model.Users;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

}
