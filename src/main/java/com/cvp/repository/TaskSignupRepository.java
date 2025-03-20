package com.cvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cvp.model.TaskSignup;

import java.time.LocalDate;
import java.util.List;

public interface TaskSignupRepository extends JpaRepository<TaskSignup, Long> {
    boolean existsByVolunteerNameAndTaskNameAndSignupDate(String volunteerName, String taskName, LocalDate signupDate);

    List<TaskSignup> findByVolunteerName(String volunteerName);

    @Query("SELECT ts FROM TaskSignup ts WHERE ts.taskName IN " +
            "(SELECT t.title FROM Task t WHERE t.org.id = :orgId)")
    List<TaskSignup> findVolunteersByOrganization(@Param("orgId") Long orgId);
}