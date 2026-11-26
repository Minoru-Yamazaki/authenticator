package br.com.authenticator.infra.config;

import br.com.authenticator.core.application.port.outbound.TokenService;
import br.com.authenticator.infra.security.JwtTokenService;
import br.com.authenticator.infra.security.RsaKeyLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.private-key-path}")
    private Resource privateKeyResource;

    @Value("${jwt.public-key-path}")
    private Resource publicKeyResource;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.issuer}")
    private String issuer;

    @Bean
    public RSAPrivateKey rsaPrivateKey() {
        return RsaKeyLoader.loadPrivateKey(privateKeyResource);
    }

    @Bean
    public RSAPublicKey rsaPublicKey() {
        return RsaKeyLoader.loadPublicKey(publicKeyResource);
    }

    @Bean
    public TokenService tokenService(RSAPrivateKey rsaPrivateKey, RSAPublicKey rsaPublicKey) {
        return new JwtTokenService(rsaPrivateKey, rsaPublicKey, accessTokenExpirationMs, issuer);
    }

}
