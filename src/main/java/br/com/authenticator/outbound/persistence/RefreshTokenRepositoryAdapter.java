package br.com.authenticator.outbound.persistence;

import br.com.authenticator.core.application.port.outbound.RefreshTokenRepository;
import br.com.authenticator.core.domain.entity.RefreshToken;
import br.com.authenticator.outbound.persistence.jpa.RefreshTokenJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;

    @Override
    public RefreshToken save(RefreshToken token) {
        return jpaRepository.save(token);
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHash(tokenHash);
    }

    @Override
    public List<RefreshToken> findByFamilyId(UUID familyId) {
        return jpaRepository.findByFamilyId(familyId);
    }

    @Override
    public void revokeAllByFamilyId(UUID familyId) {
        jpaRepository.revokeAllByFamilyId(familyId, Instant.now());
    }

    @Override
    public void revokeAllByUserId(UUID userId) {
        jpaRepository.revokeAllByUserId(userId, Instant.now());
    }

}
