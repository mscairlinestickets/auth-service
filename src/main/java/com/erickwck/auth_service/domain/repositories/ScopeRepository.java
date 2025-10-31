package com.erickwck.auth_service.domain.repositories;

import com.erickwck.auth_service.domain.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    Optional<Scope> findByName(String scopeName);
}
