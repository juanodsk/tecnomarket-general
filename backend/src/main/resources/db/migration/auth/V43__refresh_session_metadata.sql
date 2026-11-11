ALTER TABLE refresh_token ADD COLUMN IF NOT EXISTS client_description VARCHAR(255);
ALTER TABLE refresh_token ADD COLUMN IF NOT EXISTS ip_address VARCHAR(64);

CREATE INDEX IF NOT EXISTS idx_refresh_token_user_active_expiration
    ON refresh_token (user_id, revoked, expires_at, created_at);
