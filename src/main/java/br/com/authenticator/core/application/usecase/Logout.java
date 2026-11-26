package br.com.authenticator.core.application.usecase;

import br.com.authenticator.core.application.port.outbound.RefreshTokenRepository;
import br.com.authenticator.inbound.dto.request.LogoutRequest;
import lombok.AllArgsConstructor;

/**
 * Caso de uso: logout — revoga o refresh token da sessão atual.
 */
@AllArgsConstructor
public class Logout {

    private final RefreshTokenRepository refreshTokenRepository;

    public void execute(LogoutRequest request) {
        String tokenHash = Login.hashToken(request.refreshToken());

        refreshTokenRepository.findByTokenHash(tokenHash)
                .ifPresent(token -> {
                    if (!token.isRevoked()) {
                        token.revoke();
                        refreshTokenRepository.save(token);
                    }
                });
        // Sem erro caso o token não exista ou já esteja revogado — idempotente
    }

}
