package br.com.authenticator.core.application.port.outbound;

import br.com.authenticator.core.domain.entity.User;

/**
 * Porta de saída para geração e validação de Access Tokens (JWT RS256).
 */
public interface TokenService {

    /** Gera um Access Token JWT assinado com RS256 para o usuário. */
    String generateAccessToken(User user);

    /** Extrai o subject (userId) de um token válido. Lança exceção se inválido ou expirado. */
    String extractSubject(String token);

    /** Valida assinatura, emissor, audiência e expiração. */
    boolean isValid(String token);

}
