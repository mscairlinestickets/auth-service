package com.erickwck.auth_service.controller;

import com.erickwck.auth_service.utils.JwtConfig;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/api/auth")
@RestController
public class KeyPubController {

    private final JwtConfig jwtConfig;

    public KeyPubController(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> exposeJwks() {

        RSAKey jwt = new RSAKey.Builder(jwtConfig.getPublicKey())
                .keyID("auth-key-1")
                .algorithm(JWSAlgorithm.RS256)
                .keyUse(KeyUse.SIGNATURE)
                .build();
        return Map.of("keys", List.of(jwt.toPublicJWK().toJSONObject()));
    }
}
