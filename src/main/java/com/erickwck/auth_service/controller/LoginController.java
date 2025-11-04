package com.erickwck.auth_service.controller;

import com.erickwck.auth_service.controller.dto.LoginRequest;
import com.erickwck.auth_service.controller.dto.LoginResponse;
import com.erickwck.auth_service.domain.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse loginClient(@RequestBody LoginRequest loginRequest){

        return  loginService.authenticationClient(loginRequest);
    }


}
