package br.com.authenticator.infra.security;

import br.com.authenticator.core.application.port.outbound.TokenService;
import br.com.authenticator.core.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Implementação do TokenService usando JWT RS256 (JJWT 0.12.x).
 * A chave privada assina o token; a chave pública valida.
 */
@AllArgsConstructor
public class JwtTokenService implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final long accessTokenExpirationMs;
    private final String issuer;

    @Override
    public String generateAccessToken(User user) {
        long now = System.currentTimeMillis();

        String roles = user.getRoles().stream()
                .map(r -> "ROLE_" + r.name())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuer(issuer)
                .claim("roles", roles)
                .issuedAt(new Date(now))
                .expiration(new Date(now + accessTokenExpirationMs))
                .signWith(privateKey)
                .compact();
    }

    @Override
    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }

    @Override
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
