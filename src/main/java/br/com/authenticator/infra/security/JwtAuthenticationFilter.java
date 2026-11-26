package br.com.authenticator.infra.security;

import br.com.authenticator.core.application.port.outbound.TokenService;
import br.com.authenticator.core.application.port.outbound.UserRepository;
import br.com.authenticator.core.domain.entity.User;
import br.com.authenticator.core.domain.enums.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Filtro que valida o Bearer token e popula o SecurityContext.
 * O principal é o UUID do usuário para evitar consultas desnecessárias ao banco em cada request.
 */
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length());

        if (!tokenService.isValid(token)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String subjectStr = tokenService.extractSubject(token);
            UUID userId = UUID.fromString(subjectStr);

            User user = userRepository.findById(userId).orElse(null);
            if (user == null || !user.isActive()) {
                chain.doFilter(request, response);
                return;
            }

            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                    .map(Role::name)
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            log.debug("Failed to set authentication from JWT: {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }

}
