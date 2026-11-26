package br.com.authenticator.core.application.port.outbound;

import br.com.authenticator.core.domain.entity.User;
import br.com.authenticator.core.domain.valueobject.Email;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);

}
