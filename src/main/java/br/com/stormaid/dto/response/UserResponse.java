package br.com.stormaid.dto.response;

import br.com.stormaid.enums.UserStatus;

import java.util.Set;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        String phoneNumber,
        Set<String> roles,
        UserStatus status
) {}