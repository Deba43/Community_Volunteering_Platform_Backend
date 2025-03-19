package com.cvp.controller;

import com.cvp.service.OrganizationService;
import com.cvp.model.Organization;
import com.cvp.model.Task;

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

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/all")
    public List<Organization> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GetMapping("/{id}")
    public Organization getOrganization(@PathVariable Long id) {
        return organizationService.getOrganizationById(id);
    }

    @PostMapping("/register")
    public String registerOrganization(@Valid @RequestBody Organization organization, BindingResult result) {
        if (result.hasErrors()) {
            return result.getFieldErrors().get(0).getDefaultMessage();
        }
        return organizationService.registerOrganization(organization);
    }

    @PutMapping("/update")
    public String updateOrganization(@Valid @RequestBody Organization organization, BindingResult result) {
        if (result.hasErrors()) {
            return result.getFieldErrors().get(0).getDefaultMessage();
        }
        return organizationService.updateOrganization(organization);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) {
        String response = organizationService.deleteOrganization(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{org_id}/tasks")
    public ResponseEntity<List<Task>> getTasksByOrganization(@PathVariable Long org_id) {
        List<Task> tasks = organizationService.getTasksByOrganizationId(org_id);

        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tasks);
    }

}