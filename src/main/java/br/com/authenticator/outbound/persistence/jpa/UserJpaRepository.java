package br.com.authenticator.outbound.persistence.jpa;

import br.com.authenticator.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailValue(String emailValue);

    boolean existsByEmailValue(String emailValue);

}
