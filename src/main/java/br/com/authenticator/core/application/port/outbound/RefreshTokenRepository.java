package br.com.authenticator.core.application.port.outbound;

import br.com.authenticator.core.domain.entity.RefreshToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken token);

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /** Retorna todos os tokens ativos de uma família, para detecção de reutilização. */
    List<RefreshToken> findByFamilyId(UUID familyId);

    /** Revoga todos os tokens de uma família (proteção contra token reuse). */
    void revokeAllByFamilyId(UUID familyId);

    /** Revoga todos os refresh tokens ativos de um usuário (logout global). */
    void revokeAllByUserId(UUID userId);

}
