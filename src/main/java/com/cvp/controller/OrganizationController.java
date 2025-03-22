package com.cvp.controller;

import com.cvp.service.OrganizationService;
import com.cvp.service.TaskSignupService;
import com.cvp.exception.InvalidEntityException;
import com.cvp.model.Organization;
import com.cvp.model.Task;
import com.cvp.model.TaskSignup;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@CrossOrigin(origins = "*")
public class OrganizationController {

    @Autowired
    private final OrganizationService organizationService;

    @Autowired
    private final TaskSignupService taskSignupService;

    OrganizationController(OrganizationService organizationService, TaskSignupService taskSignupService) {
        this.organizationService = organizationService;
        this.taskSignupService = taskSignupService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> orgs = organizationService.getAllOrganizations();
        return ResponseEntity.ok(orgs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable Long id) throws InvalidEntityException {
        Organization organization = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(organization);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerOrganization(@Valid @RequestBody Organization organization, BindingResult result)
            throws InvalidEntityException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors().get(0).getDefaultMessage());
        }
        String response = organizationService.registerOrganization(organization);
        if (response.contains("successful")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrganization(@Valid @RequestBody Organization organization, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors().get(0).getDefaultMessage());
        }
        String response = organizationService.updateOrganization(organization);
        if (response.contains("successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) throws InvalidEntityException {
        String response = organizationService.deleteOrganization(id);
        if (response.contains("deleted successfully")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{org_id}/tasks")
    public ResponseEntity<List<Task>> getTasksByOrganization(@PathVariable Long org_id) {
        List<Task> tasks = organizationService.getTasksByOrganizationId(org_id);

        if (tasks.isEmpty()) {
            throw new InvalidEntityException("No Task found for this Organization : " + org_id);
        }

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{org_id}/volunteers")
    public ResponseEntity<List<TaskSignup>> getVolunteersByOrganization(@PathVariable Long org_id) {
        List<TaskSignup> volunteers = taskSignupService.getVolunteersByOrganization(org_id);

        if (volunteers.isEmpty()) {
            throw new InvalidEntityException("No Volunteers found for this Organization : " + org_id);
        }

        return ResponseEntity.ok(volunteers);
    }

}