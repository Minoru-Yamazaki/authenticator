package br.com.authenticator.core.domain.listener;

import br.com.authenticator.core.domain.entity.DomainEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DomainEntityListener {

	@PrePersist
	public void prePersist(Object entity) {
		DomainEntity domainEntity = (DomainEntity) entity;
		domainEntity.setId(UUID.randomUUID());
		domainEntity.setCreatedAt(Optional.ofNullable(domainEntity.getCreatedAt()).orElse(Instant.now()));
		domainEntity.setUpdatedAt(Instant.now());
	}

	@PreUpdate
	public void preUpdate(Object entity) {
		DomainEntity domainEntity = (DomainEntity) entity;
		domainEntity.setUpdatedAt(Instant.now());
	}

}
