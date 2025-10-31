package com.erickwck.auth_service.demo;

import com.erickwck.auth_service.domain.entity.AirlineCompany;
import com.erickwck.auth_service.domain.entity.Role;
import com.erickwck.auth_service.domain.entity.Scope;
import com.erickwck.auth_service.domain.repositories.AirlineCompanyRepository;
import com.erickwck.auth_service.domain.repositories.RoleRepository;
import com.erickwck.auth_service.domain.repositories.ScopeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.erickwck.auth_service.domain.entity.Role.RoleNames.ROLE_AIRLINE;
import static com.erickwck.auth_service.domain.entity.Scope.SecurityScopes.*;

@Component
public class RBACSeed implements CommandLineRunner {

    private final ScopeRepository scopeRepository;
    private final RoleRepository roleRepository;
    private final AirlineCompanyRepository airlineCompanyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public RBACSeed(ScopeRepository scopeRepository, RoleRepository roleRepository, AirlineCompanyRepository airlineCompanyRepository) {
        this.scopeRepository = scopeRepository;
        this.roleRepository = roleRepository;
        this.airlineCompanyRepository = airlineCompanyRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {

        Scope writeScope = ensureScope(SCOPE_FLIGHT_WRITE);
        Scope updateScope = ensureScope(SCOPE_FLIGHT_UPDATE);
        Scope deleteScope = ensureScope(SCOPE_FLIGHT_DELETE);
        Scope bookingRead = ensureScope(SCOPE_BOOKING_READ);

        Role roleAirlineAdmin = ensureRole(ROLE_AIRLINE, Set.of(writeScope, updateScope, deleteScope,bookingRead));

        AirlineCompany golAirline = ensureAirlineCompany("Gol Linhas Aéreas", "gol_air", "senha", "contato@gol.com", roleAirlineAdmin);
        AirlineCompany azulAirline = ensureAirlineCompany("Azul Linhas Aéreas", "azul_air", "senha", "contato@azul.com", roleAirlineAdmin);
        AirlineCompany latamAirline = ensureAirlineCompany("Latam Linhas Aéreas", "latam_air", "senha", "contato@latam.com", roleAirlineAdmin);
    }

    private Role ensureRole(String name, Set<Scope> scopes) {

        return roleRepository.findByName(name)
                .map(existngRole -> {
                    existngRole.setScopes(scopes);
                    return roleRepository.save(existngRole);
                })
                .orElseGet(() -> roleRepository.save(new Role(null, name, scopes)));

    }

    private Scope ensureScope(String scopeName) {

        return scopeRepository.findByName(scopeName)
                .orElseGet(() -> scopeRepository.save(new Scope(null, scopeName)));

    }

    private AirlineCompany ensureAirlineCompany(String name, String username, String password, String email, Role roles) {

        return airlineCompanyRepository.findByUsername(username)
                .map(existingAirlineCompany -> {
                    existingAirlineCompany.setPassword((bCryptPasswordEncoder.encode(password)));
                    existingAirlineCompany.setRoles(Set.of(roles));
                    return airlineCompanyRepository.save(existingAirlineCompany);
                })
                .orElseGet(() -> airlineCompanyRepository
                        .save(new AirlineCompany(name, username, bCryptPasswordEncoder.encode(password), email, Set.of(roles))));
    }
}
