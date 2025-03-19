package com.cvp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cvp.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
    Organization findByEmail(String email);

}
