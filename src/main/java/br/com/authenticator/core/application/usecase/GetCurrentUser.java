package br.com.authenticator.core.application.usecase;

import br.com.authenticator.core.application.port.outbound.UserRepository;
import br.com.authenticator.core.domain.entity.User;
import br.com.authenticator.core.domain.exceptions.DomainException;
import br.com.authenticator.inbound.dto.response.UserResponse;
import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * Caso de uso: retorna os dados básicos do usuário autenticado.
 */
@AllArgsConstructor
public class GetCurrentUser {

    private final UserRepository userRepository;

    public UserResponse execute(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> DomainException.userNotFound(userId));
        return UserResponse.from(user);
    }

}
