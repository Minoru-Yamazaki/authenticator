package br.com.authenticator.inbound.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}
