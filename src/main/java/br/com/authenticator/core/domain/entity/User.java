package br.com.authenticator.core.domain.entity;

import br.com.authenticator.core.domain.enums.Role;
import br.com.authenticator.core.domain.enums.UserStatus;
import br.com.authenticator.core.domain.valueobject.Email;
import br.com.authenticator.core.domain.valueobject.PasswordHash;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends DomainEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password_hash", nullable = false))
    private PasswordHash passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Set<Role> roles = EnumSet.of(Role.USER);

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void changePassword(PasswordHash newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

}
