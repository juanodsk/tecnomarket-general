ALTER TABLE email_verification_token
    DROP CONSTRAINT IF EXISTS idx_email_verification_token_token;

DROP INDEX IF EXISTS idx_email_verification_token_token;

ALTER TABLE email_verification_token
    RENAME COLUMN token TO token_hash;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

UPDATE email_verification_token
SET token_hash = encode(digest(token_hash, 'sha256'), 'hex');

ALTER TABLE email_verification_token
    ALTER COLUMN token_hash TYPE VARCHAR(64);

CREATE UNIQUE INDEX IF NOT EXISTS idx_email_verification_token_hash
    ON email_verification_token(token_hash);
