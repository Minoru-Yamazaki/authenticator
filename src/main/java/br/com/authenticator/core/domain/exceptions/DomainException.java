package br.com.authenticator.core.domain.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DomainException extends RuntimeException {

	private final String code;

	private final int statusCode;

	private String type;

	private String detail;

	public DomainException(String code, String message, int statusCode) {
		super(message);
		this.code = code;
		this.statusCode = statusCode;
	}

	public DomainException(String code, String message) {
		super(message);
		this.code = code;
		this.statusCode = 400;
	}

	public DomainException(String code, String message, int statusCode, String type, String detail) {
		super(message);
		this.code = code;
		this.statusCode = statusCode;
		this.type = type;
		this.detail = detail;
	}

	public static DomainException of(String code, String message) {
		return new DomainException(code, message);
	}

	public static DomainException userNotFound(UUID userId) {
		return new DomainException("COD-001", "User not found: " + userId, 404,
				"urn:authentication-service:user-not-found",
				"No user exists with the given identifier.");
	}

	public static DomainException invalidCredentials() {
		return new DomainException("COD-002", "Authentication failed", 401,
				"urn:authentication-service:authentication-failed",
				"Invalid credentials or token.");
	}

	public static DomainException refreshTokenReuseDetected() {
		return new DomainException("COD-003", "Token reuse detected", 401,
				"urn:authentication-service:token-reuse-detected",
				"All sessions have been revoked for security.");
	}

	public static DomainException passwordPolicyViolation(String detail) {
		return new DomainException("COD-004", "Password policy violation", 422,
				"urn:authentication-service:password-policy-violation",
				detail);
	}

	public static DomainException emailAlreadyInUse() {
		return new DomainException("COD-005", "Email already in use", 409,
				"urn:authentication-service:email-conflict",
				"An account with this email already exists.");
	}

}
