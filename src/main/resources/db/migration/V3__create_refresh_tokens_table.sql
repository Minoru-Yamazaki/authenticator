CREATE TABLE refresh_tokens
(
    id                  BINARY(16)   NOT NULL,
    user_id             BINARY(16)   NOT NULL,
    family_id           BINARY(16)   NOT NULL,
    token_hash          VARCHAR(255) NOT NULL,
    expires_at          DATETIME(6)  NOT NULL,
    revoked_at          DATETIME(6)  NULL,
    replaced_by_token_id BINARY(16)  NULL,
    created_at          DATETIME(6)  NOT NULL,
    updated_at          DATETIME(6)  NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id),
    CONSTRAINT uq_refresh_tokens_hash UNIQUE (token_hash),
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);
CREATE INDEX idx_refresh_tokens_family_id ON refresh_tokens (family_id);
