package br.com.authenticator.inbound.dto.response;

import br.com.authenticator.core.domain.entity.User;
import br.com.authenticator.core.domain.enums.Role;

import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        Set<Role> roles
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail().getValue(),
                user.getRoles()
        );
    }
}
