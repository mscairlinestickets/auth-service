package com.erickwck.auth_service.controller.dto;

import java.util.List;

public record LoginResponse(
        String accessToken,
        Long expiresIn,
        List<String> scopes

) {
}
