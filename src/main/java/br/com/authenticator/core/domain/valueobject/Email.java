package br.com.authenticator.core.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class Email {

    private String value;

    private Email(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        this.value = raw.trim().toLowerCase();
    }

    public static Email of(String raw) {
        return new Email(raw);
    }

    @Override
    public String toString() {
        return value;
    }

}
