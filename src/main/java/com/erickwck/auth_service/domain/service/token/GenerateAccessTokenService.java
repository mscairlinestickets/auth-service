package com.erickwck.auth_service.domain.service.token;

import com.erickwck.auth_service.domain.entity.AirlineCompany;
import com.erickwck.auth_service.domain.entity.Role;
import com.erickwck.auth_service.domain.entity.Scope;
import com.erickwck.auth_service.domain.repositories.AirlineCompanyRepository;
import com.erickwck.auth_service.utils.JwtConfig;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GenerateAccessTokenService {

    private final JwtConfig jwtConfig;
    private final AirlineCompanyRepository airlineCompanyRepository;
    private final JwtEncoder jwtEncoder;

    public GenerateAccessTokenService(JwtConfig jwtConfig, AirlineCompanyRepository airlineCompanyRepository, JwtEncoder jwtEncoder) {
        this.jwtConfig = jwtConfig;
        this.airlineCompanyRepository = airlineCompanyRepository;
        this.jwtEncoder = jwtEncoder;
    }


    public Jwt generateAccessToken(AirlineCompany userAirlineCompany) {

        var roles = getRolesUsers(userAirlineCompany);

        var scopes = getScopesUser(userAirlineCompany);

        var claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .subject(userAirlineCompany.getId().toString())
                .issuer(jwtConfig.getIssuer())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtConfig.getExpiresIn()))
                .claim("username", userAirlineCompany.getUsername())
                .claim("roles", roles)
                .claim("scope", scopes).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }


    private static Set<String> getRolesUsers(AirlineCompany airlineCompany) {

        return airlineCompany.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    private static Set<String> getScopesUser(AirlineCompany airlineCompany) {

        Set<Scope> mergedScopes = airlineCompany.getRoles()
                .stream().map(Role::getScopes)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));

        mergedScopes.addAll(airlineCompany.getScopes());

        return mergedScopes.stream().map(Scope::getName).collect(Collectors.toSet());

    }

}
