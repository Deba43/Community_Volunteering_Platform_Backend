package com.cvp.service;

import com.cvp.repository.TaskSignupRepository;
import com.cvp.model.TaskSignup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskSignupService {
    private final TaskSignupRepository taskSignupRepository;

    @Autowired
    private EmailService mailSender;

    @Autowired
    public TaskSignupService(TaskSignupRepository taskSignupRepository) {
        this.taskSignupRepository = taskSignupRepository;
    }

    public List<TaskSignup> getAllSignups() {
        return taskSignupRepository.findAll();
    }

    public String registerForTask(TaskSignup taskSignup) {
        if (taskSignup.getSignupDate() == null) {
            taskSignup.setSignupDate(LocalDate.now());
        }

        boolean alreadyRegistered = taskSignupRepository.existsByVolunteerNameAndTaskNameAndSignupDate(
                taskSignup.getVolunteerName(), taskSignup.getTaskName(), taskSignup.getSignupDate());

        if (alreadyRegistered) {
            return "You have already registered for this task on the selected date.";
        } else {
            taskSignupRepository.save(taskSignup);

            // Send confirmation email with details
            mailSender.sendEmailForTaskSignUp(
                    "devcloud48@gmail.com", // Put your gmail
                    taskSignup.getTaskName(),
                    taskSignup.getVolunteerName(),
                    taskSignup.getSignupDate());

            return "Registration Successful!";

        }

    }

    public List<TaskSignup> getVolunteersByOrganization(Long orgId) {
        return taskSignupRepository.findVolunteersByOrganization(orgId);
    }

}
