package br.com.authenticator.infra.config;

import br.com.authenticator.core.application.port.outbound.PasswordEncoder;
import br.com.authenticator.core.application.port.outbound.RefreshTokenRepository;
import br.com.authenticator.core.application.port.outbound.TokenService;
import br.com.authenticator.core.application.port.outbound.UserRepository;
import br.com.authenticator.core.application.usecase.*;
import br.com.authenticator.infra.security.BcryptPasswordEncoderAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BcryptPasswordEncoderAdapter(12);
    }

    @Bean
    public RegisterUser registerUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new RegisterUser(userRepository, passwordEncoder);
    }

    @Bean
    public Login loginUseCase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            RefreshTokenRepository refreshTokenRepository) {
        return new Login(userRepository, passwordEncoder, tokenService, refreshTokenRepository, refreshTokenExpirationMs);
    }

    @Bean
    public RefreshToken refreshTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            TokenService tokenService) {
        return new RefreshToken(refreshTokenRepository, userRepository, tokenService, refreshTokenExpirationMs);
    }

    @Bean
    public Logout logoutUseCase(RefreshTokenRepository refreshTokenRepository) {
        return new Logout(refreshTokenRepository);
    }

    @Bean
    public GetCurrentUser getCurrentUserUseCase(UserRepository userRepository) {
        return new GetCurrentUser(userRepository);
    }

}
