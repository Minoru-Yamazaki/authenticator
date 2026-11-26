package br.com.authenticator.inbound.exception;

import br.com.authenticator.core.domain.exceptions.DomainException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Tratamento centralizado de erros seguindo RFC 9457 (Problem Details for HTTP APIs).
 * Nenhum detalhe interno é exposto — apenas mensagens seguras e controladas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(422);
        problem.setType(URI.create("urn:authentication-service:validation-error"));
        problem.setTitle("Validation failed");

        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        problem.setDetail(errors);
        return problem;
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(ex.getStatusCode());
        problem.setType(URI.create(ex.getType()));
        problem.setTitle(ex.getMessage());
        problem.setDetail(ex.getDetail());
        problem.setProperty("code", ex.getCode());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        // Nunca expõe detalhes de erro interno
        ProblemDetail problem = ProblemDetail.forStatus(500);
        problem.setType(URI.create("urn:authentication-service:internal-error"));
        problem.setTitle("Internal server error");
        return problem;
    }

}
