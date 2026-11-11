DROP INDEX IF EXISTS idx_refresh_token_token;

ALTER TABLE refresh_token
    RENAME COLUMN token TO token_hash;

ALTER TABLE refresh_token
    ADD COLUMN family_id UUID,
    ADD COLUMN revoked_at TIMESTAMP,
    ADD COLUMN last_used_at TIMESTAMP,
    ADD COLUMN replaced_by_token_hash VARCHAR(64);

CREATE EXTENSION IF NOT EXISTS pgcrypto;

UPDATE refresh_token
SET token_hash = encode(digest(token_hash, 'sha256'), 'hex'),
    family_id = gen_random_uuid();

ALTER TABLE refresh_token
    ALTER COLUMN token_hash TYPE VARCHAR(64),
    ALTER COLUMN family_id SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_refresh_token_hash
    ON refresh_token(token_hash);

CREATE INDEX idx_refresh_token_family_id
    ON refresh_token(family_id);

CREATE INDEX idx_refresh_token_user_active
    ON refresh_token(user_id, expires_at)
    WHERE revoked = FALSE;
