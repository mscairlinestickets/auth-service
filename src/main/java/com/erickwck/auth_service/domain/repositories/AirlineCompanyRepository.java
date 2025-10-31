package com.erickwck.auth_service.domain.repositories;

import com.erickwck.auth_service.domain.entity.AirlineCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirlineCompanyRepository  extends JpaRepository<AirlineCompany, Long> {

    Optional<AirlineCompany> findByUsername(String username);
}
