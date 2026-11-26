package br.com.authenticator.core.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class PasswordHash {

    private String value;

    private PasswordHash(String hash) {
        if (hash == null || hash.isBlank()) {
            throw new IllegalArgumentException("Password hash must not be blank");
        }
        this.value = hash;
    }

    public static PasswordHash of(String hash) {
        return new PasswordHash(hash);
    }

}
