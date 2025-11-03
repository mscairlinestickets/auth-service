package com.erickwck.auth_service.domain.service;

import com.erickwck.auth_service.controller.dto.LoginRequest;
import com.erickwck.auth_service.controller.dto.LoginResponse;
import com.erickwck.auth_service.domain.entity.AirlineCompany;
import com.erickwck.auth_service.domain.repositories.AirlineCompanyRepository;
import com.erickwck.auth_service.domain.service.token.GenerateAccessTokenService;
import com.erickwck.auth_service.exceptions.AirlineCompanyNotFound;
import com.erickwck.auth_service.exceptions.AirlineCompanyUsernameOrPAsswordInvalid;
import com.erickwck.auth_service.utils.JwtConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AirlineCompanyRepository airlineCompanyRepository;
    private final GenerateAccessTokenService accessTokenService;
    private final JwtConfig jwtConfig;

    public LoginService(BCryptPasswordEncoder bCryptPasswordEncoder, AirlineCompanyRepository airlineCompanyRepository, GenerateAccessTokenService accessTokenService, JwtConfig jwtConfig) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.airlineCompanyRepository = airlineCompanyRepository;
        this.accessTokenService = accessTokenService;
        this.jwtConfig = jwtConfig;
    }

    @Transactional
    public LoginResponse authenticationClient(LoginRequest loginRequest) {
        var user = validateUser(loginRequest.username(), loginRequest.password());

        var accessToken = accessTokenService.generateAccessToken(user);

        return new LoginResponse(
                accessToken.getTokenValue(),
                jwtConfig.getExpiresIn(),
                accessToken.getClaimAsStringList("scope"));
    }

    public AirlineCompany validateUser(String username, String password) {
        var user = airlineCompanyRepository.findByUsername(username).orElseThrow(() -> new AirlineCompanyNotFound(username));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new AirlineCompanyUsernameOrPAsswordInvalid();
        }
        return user;
    }
}
