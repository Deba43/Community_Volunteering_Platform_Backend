package com.cvp.service;

import com.cvp.repository.OrganizationRepository;
import com.cvp.repository.TaskRepository;

import jakarta.validation.Valid;

import com.cvp.exception.InvalidEntityException;
import com.cvp.model.Organization;
import com.cvp.model.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, PasswordEncoder passwordEncoder) {
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    TaskRepository taskRepository;

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new InvalidEntityException("Organization with ID " + id + " not found."));
    }

    public boolean existsById(Long org_id) {
        return organizationRepository.existsById(org_id);
    }

    public String registerOrganization(@Valid Organization organization) throws InvalidEntityException {
        boolean alreadyExists = organizationRepository.existsByEmailOrPhoneNumber(
                organization.getEmail(), organization.getPhoneNumber());
        if (alreadyExists) {
            throw new InvalidEntityException("An organization with this email or phone number already exists.");
        } else {
            // Encode the raw password before saving
            organization.setPassword(passwordEncoder.encode(organization.getPassword()));
            organizationRepository.save(organization);

            // Prepare the welcome email details
            String subject = "Welcome to Our Platform!";
            String text = "Dear " + organization.getName() + ",\n\n" +
                    "Thank you for registering your organization with us. " +
                    "We are excited to have you on board.\n\n" +
                    "Best Regards,\n" +
                    "Team 3";

            try {
                emailService.sendEmail(organization.getEmail(), subject, text);
            } catch (Exception e) {
                logger.error("Error sending welcome email to {}: {}", organization.getEmail(), e.getMessage());

            }

            return "Organization registration successful!";
        }
    }

    public String updateOrganization(@Valid Organization organization) {
        Optional<Organization> existingOrg = organizationRepository.findById(organization.getId());
        if (existingOrg.isPresent()) {

            if (!organization.getPassword().equals(existingOrg.get().getPassword())) {
                organization.setPassword(passwordEncoder.encode(organization.getPassword()));
            }
            organizationRepository.save(organization);
            return "Organization updated successfully!";
        } else {
            return "Organization not found.";
        }
    }

    public String deleteOrganization(Long id) throws InvalidEntityException {
        if (!organizationRepository.existsById(id)) {
            throw new InvalidEntityException("Organization with ID " + id + " not found.");
        }
        organizationRepository.deleteById(id);
        return "Organization deleted successfully!";
    }

    public List<Task> getTasksByOrganizationId(Long org_id) {
        List<Task> tasks = taskRepository.findByOrg_Id(org_id);

        if (tasks.isEmpty()) {
            throw new InvalidEntityException("No tasks found for Organization with ID " + org_id);
        }

        return tasks;
    }

    public Organization login(String email, String rawPassword) {
        Organization organization = organizationRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, organization.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return organization;
    }

}
