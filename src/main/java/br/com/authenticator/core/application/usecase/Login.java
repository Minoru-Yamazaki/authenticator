package br.com.authenticator.core.application.usecase;

import br.com.authenticator.core.application.port.outbound.PasswordEncoder;
import br.com.authenticator.core.application.port.outbound.RefreshTokenRepository;
import br.com.authenticator.core.application.port.outbound.TokenService;
import br.com.authenticator.core.application.port.outbound.UserRepository;
import br.com.authenticator.core.domain.entity.RefreshToken;
import br.com.authenticator.core.domain.entity.User;
import br.com.authenticator.core.domain.exceptions.DomainException;
import br.com.authenticator.core.domain.valueobject.Email;
import br.com.authenticator.inbound.dto.request.LoginRequest;
import br.com.authenticator.inbound.dto.response.TokenResponse;
import lombok.AllArgsConstructor;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

/**
 * Caso de uso: autenticação por email e senha.
 * Erros de credencial retornam exceção genérica para evitar enumeração de usuários.
 */
@AllArgsConstructor
public class Login {

    private static final int REFRESH_TOKEN_BYTES = 32; // 256 bits de entropia
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenExpirationMs;

    public TokenResponse execute(LoginRequest request) {
        Email email = Email.of(request.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(DomainException::invalidCredentials);

        if (!user.isActive()) {
            throw DomainException.invalidCredentials();
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash().getValue())) {
            throw DomainException.invalidCredentials();
        }

        String accessToken = tokenService.generateAccessToken(user);
        String rawRefreshToken = generateRawToken();
        String refreshTokenHash = hashToken(rawRefreshToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .familyId(UUID.randomUUID())
                .tokenHash(refreshTokenHash)
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .build();

        refreshTokenRepository.save(refreshToken);

        return new TokenResponse(accessToken, rawRefreshToken);
    }

    private String generateRawToken() {
        byte[] bytes = new byte[REFRESH_TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    static String hashToken(String raw) {
        try {
            var digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

}
