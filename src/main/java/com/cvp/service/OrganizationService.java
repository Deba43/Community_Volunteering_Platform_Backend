package com.cvp.service;

import com.cvp.repository.OrganizationRepository;
import com.cvp.repository.TaskRepository;
import com.cvp.exception.InvalidEntityException;
import com.cvp.model.Organization;
import com.cvp.model.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
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

    public String registerOrganization(Organization organization) {
        if (organizationRepository.existsByEmailOrPhoneNumber(organization.getEmail(), organization.getPhoneNumber())) {
            throw new InvalidEntityException("An organization with this email or phone number already exists.");
        }
        organizationRepository.save(organization);
        return "Organization registered successfully";
    }

    public String updateOrganization(Organization updatedOrganization) {
        Organization existingOrg = getOrganizationById(updatedOrganization.getId());
        existingOrg.setName(updatedOrganization.getName());
        existingOrg.setWebsite(updatedOrganization.getWebsite());
        existingOrg.setLocation(updatedOrganization.getLocation());
        existingOrg.setEmail(updatedOrganization.getEmail());
        existingOrg.setPhoneNumber(updatedOrganization.getPhoneNumber());
        organizationRepository.save(existingOrg);
        return "Organization updated successfully";
    }

    public String deleteOrganization(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new InvalidEntityException("Organization with ID " + id + " not found.");
        }
        organizationRepository.deleteById(id);
        return "Organization deleted successfully";
    }

    public List<Task> getTasksByOrganizationId(Long org_id) {
        List<Task> tasks = taskRepository.findByOrg_Id(org_id);
        
        if (tasks.isEmpty()) {
            throw new InvalidEntityException("No tasks found for Organization with ID " + org_id);
        }
        
        return tasks;
    }
    

}
