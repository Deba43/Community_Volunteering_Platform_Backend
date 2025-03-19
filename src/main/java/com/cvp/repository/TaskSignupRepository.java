package com.cvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cvp.model.TaskSignup;

import java.time.LocalDate;
import java.util.List;

public interface TaskSignupRepository extends JpaRepository<TaskSignup, Long> {
    boolean existsByVolunteerNameAndTaskNameAndSignupDate(String volunteerName, String taskName, LocalDate signupDate);

    List<TaskSignup> findByVolunteerName(String volunteerName);
}