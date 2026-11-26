package br.com.authenticator.infra.security;

import br.com.authenticator.core.application.port.outbound.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Adapta o BCryptPasswordEncoder do Spring para a porta PasswordEncoder do domínio.
 */
public class BcryptPasswordEncoderAdapter implements PasswordEncoder {

    private final BCryptPasswordEncoder delegate;

    public BcryptPasswordEncoderAdapter(int strength) {
        this.delegate = new BCryptPasswordEncoder(strength);
    }

    @Override
    public String encode(String rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }

}
