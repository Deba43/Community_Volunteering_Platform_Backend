package com.cvp.controller;

import com.cvp.service.TaskSignupService;
import com.cvp.model.TaskSignup;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasksignup")
public class TaskSignupController {

    private final TaskSignupService taskSignupService;

    @Autowired
    public TaskSignupController(TaskSignupService taskSignupService) {
        this.taskSignupService = taskSignupService;
    }

    @GetMapping("/all")
    public List<TaskSignup> getAllSignups() {
        return taskSignupService.getAllSignups();
    }

    @PostMapping("/register")
    public String registerTask(@Valid @RequestBody TaskSignup taskSignup, BindingResult result) {
        if (result.hasErrors()) {
            return result.getFieldErrors().get(0).getDefaultMessage(); // Returns the first validation error message
        }

        return taskSignupService.registerForTask(taskSignup);
    }
}

