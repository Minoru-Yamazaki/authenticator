package br.com.authenticator.core.application.usecase;

import br.com.authenticator.core.application.port.outbound.RefreshTokenRepository;
import br.com.authenticator.core.application.port.outbound.TokenService;
import br.com.authenticator.core.application.port.outbound.UserRepository;
import br.com.authenticator.core.domain.entity.User;
import br.com.authenticator.core.domain.exceptions.DomainException;
import br.com.authenticator.inbound.dto.request.RefreshRequest;
import br.com.authenticator.inbound.dto.response.TokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

/**
 * Caso de uso: renovação de Access Token via Refresh Token.
 *
 * Proteção contra reutilização (refresh token rotation):
 * - Se um token válido é apresentado, ele é revogado e um novo é emitido na mesma transação.
 * - Se um token já revogado é apresentado, toda a família é revogada (detecção de roubo).
 *
 * A revogação do token antigo e a persistência do novo ocorrem na mesma transação.
 */
@AllArgsConstructor
public class RefreshToken {

    private static final int REFRESH_TOKEN_BYTES = 32;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final long refreshTokenExpirationMs;

    @Transactional
    public TokenResponse execute(RefreshRequest request) {
        String incomingHash = Login.hashToken(request.refreshToken());

        br.com.authenticator.core.domain.entity.RefreshToken existing = refreshTokenRepository.findByTokenHash(incomingHash)
                .orElseThrow(DomainException::invalidCredentials);

        if (existing.isRevoked()) {
            // Token já foi usado — possível roubo, revoga toda a família
            refreshTokenRepository.revokeAllByFamilyId(existing.getFamilyId());
            throw DomainException.refreshTokenReuseDetected();
        }

        if (existing.isExpired()) {
            throw DomainException.invalidCredentials();
        }

        User user = userRepository.findById(existing.getUserId())
                .orElseThrow(DomainException::invalidCredentials);

        if (!user.isActive()) {
            throw DomainException.invalidCredentials();
        }

        // Emite novo refresh token na mesma família
        String rawNew = generateRawToken();
        String hashNew = Login.hashToken(rawNew);

        br.com.authenticator.core.domain.entity.RefreshToken newToken = br.com.authenticator.core.domain.entity.RefreshToken.builder()
                .userId(user.getId())
                .familyId(existing.getFamilyId())
                .tokenHash(hashNew)
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .build();

        br.com.authenticator.core.domain.entity.RefreshToken saved = refreshTokenRepository.save(newToken);

        // Revoga o token antigo apontando para o novo — tudo na mesma transação
        existing.revoke(saved.getId());
        refreshTokenRepository.save(existing);

        String accessToken = tokenService.generateAccessToken(user);
        return new TokenResponse(accessToken, rawNew);
    }

    private String generateRawToken() {
        byte[] bytes = new byte[REFRESH_TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
