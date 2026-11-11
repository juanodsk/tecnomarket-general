ALTER TABLE app_user
    ADD COLUMN IF NOT EXISTS security_version INTEGER NOT NULL DEFAULT 0;

ALTER TABLE app_user
    DROP CONSTRAINT IF EXISTS ck_app_user_security_version_non_negative;

ALTER TABLE app_user
    ADD CONSTRAINT ck_app_user_security_version_non_negative
        CHECK (security_version >= 0);

ALTER TABLE refresh_token
    ADD COLUMN IF NOT EXISTS issued_security_version INTEGER;

UPDATE refresh_token token
SET issued_security_version = app_user.security_version
FROM app_user
WHERE token.user_id = app_user.id
  AND token.issued_security_version IS NULL;

ALTER TABLE refresh_token
    ALTER COLUMN issued_security_version SET NOT NULL;
