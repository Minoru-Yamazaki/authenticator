package br.com.authenticator.core.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends DomainEntity {

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID userId;

    /**
     * Token family ID — usado para detectar reutilização de refresh token.
     * Todos os tokens de uma cadeia de renovação compartilham o mesmo familyId.
     * Se um token revogado for apresentado, toda a família é revogada.
     */
    @Column(name = "family_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID familyId;

    /** SHA-256 do token raw; nunca armazene o valor bruto. */
    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "replaced_by_token_id", columnDefinition = "BINARY(16)")
    private UUID replacedByTokenId;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isValid() {
        return !isRevoked() && !isExpired();
    }

    /**
     * Revoga este token e registra qual token o substituiu.
     * Deve ser chamado dentro de uma transação junto com a persistência do novo token.
     */
    public void revoke(UUID replacedByTokenId) {
        this.revokedAt = Instant.now();
        this.replacedByTokenId = replacedByTokenId;
    }

    /**
     * Revoga este token sem substituto (ex: logout).
     */
    public void revoke() {
        this.revokedAt = Instant.now();
    }

}
