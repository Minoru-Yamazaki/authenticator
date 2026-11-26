package br.com.authenticator.core.application.port.outbound;

/**
 * Porta de saída para codificação e verificação de senhas.
 * O domínio depende desta abstração, não do BCryptPasswordEncoder do Spring diretamente.
 */
public interface PasswordEncoder {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);

}
