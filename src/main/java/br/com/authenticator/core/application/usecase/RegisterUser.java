package br.com.authenticator.core.application.usecase;

import br.com.authenticator.core.application.port.outbound.PasswordEncoder;
import br.com.authenticator.core.application.port.outbound.UserRepository;
import br.com.authenticator.core.domain.entity.User;
import br.com.authenticator.core.domain.enums.Role;
import br.com.authenticator.core.domain.exceptions.DomainException;
import br.com.authenticator.core.domain.valueobject.Email;
import br.com.authenticator.core.domain.valueobject.PasswordHash;
import br.com.authenticator.inbound.dto.request.RegisterRequest;
import br.com.authenticator.inbound.dto.response.UserResponse;
import lombok.AllArgsConstructor;

import java.util.EnumSet;

/**
 * Caso de uso: cadastro público de usuário.
 * Sempre atribui a role USER — cadastros com ADMIN devem ser feitos por endpoints restritos.
 */
@AllArgsConstructor
public class RegisterUser {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse execute(RegisterRequest request) {
        Email email = Email.of(request.email());

        if (userRepository.existsByEmail(email)) {
            throw DomainException.emailAlreadyInUse();
        }

        validatePassword(request.password());

        PasswordHash hash = PasswordHash.of(passwordEncoder.encode(request.password()));

        User user = User.builder()
                .name(request.name())
                .email(email)
                .passwordHash(hash)
                .roles(EnumSet.of(Role.USER))
                .build();

        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw DomainException.passwordPolicyViolation("Password must be at least 8 characters.");
        }
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        if (!hasUpper || !hasDigit) {
            throw DomainException.passwordPolicyViolation(
                    "Password must contain at least one uppercase letter and one digit.");
        }
    }

}
