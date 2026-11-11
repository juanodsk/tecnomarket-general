ALTER TABLE business
    ADD COLUMN logo_bucket_name VARCHAR(100),
    ADD COLUMN logo_object_key VARCHAR(700),
    ADD COLUMN cover_image_bucket_name VARCHAR(100),
    ADD COLUMN cover_image_object_key VARCHAR(700);

ALTER TABLE business_service
    ADD COLUMN image_bucket_name VARCHAR(100);

ALTER TABLE repair_order_attachment
    ADD COLUMN checksum_sha256 CHAR(64),
    ADD COLUMN deleted_by_business_user_id INTEGER,
    ADD COLUMN retention_until TIMESTAMP;

ALTER TABLE repair_order_attachment
    ADD CONSTRAINT fk_deleted_by_business_user_id_repair_order_attachment
        FOREIGN KEY (deleted_by_business_user_id) REFERENCES business_user (id),
    ADD CONSTRAINT ck_repair_order_attachment_checksum
        CHECK (checksum_sha256 IS NULL OR checksum_sha256 ~ '^[0-9a-f]{64}$'),
    ADD CONSTRAINT ck_repair_order_attachment_retention
        CHECK (retention_until IS NULL OR deleted_at IS NOT NULL);

CREATE TABLE storage_object_deletion_task (
    id SERIAL PRIMARY KEY,
    bucket_name VARCHAR(100) NOT NULL,
    object_key VARCHAR(700) NOT NULL,
    reason VARCHAR(50) NOT NULL,
    source_type VARCHAR(50) NOT NULL,
    source_id INTEGER,
    scheduled_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    attempt_count INTEGER NOT NULL DEFAULT 0,
    last_error VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    CONSTRAINT ck_storage_deletion_status CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')),
    CONSTRAINT ck_storage_deletion_attempt_count CHECK (attempt_count >= 0)
);

CREATE INDEX idx_storage_deletion_due
    ON storage_object_deletion_task (status, scheduled_at, id);

CREATE UNIQUE INDEX uq_storage_deletion_pending_object
    ON storage_object_deletion_task (bucket_name, object_key)
    WHERE status = 'PENDING';

CREATE OR REPLACE FUNCTION prevent_repair_order_attachment_content_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.repair_order_id IS DISTINCT FROM OLD.repair_order_id
        OR NEW.uploaded_by_business_user_id IS DISTINCT FROM OLD.uploaded_by_business_user_id
        OR NEW.file_url IS DISTINCT FROM OLD.file_url
        OR NEW.bucket_name IS DISTINCT FROM OLD.bucket_name
        OR NEW.object_key IS DISTINCT FROM OLD.object_key
        OR NEW.original_file_name IS DISTINCT FROM OLD.original_file_name
        OR NEW.content_type IS DISTINCT FROM OLD.content_type
        OR NEW.file_size IS DISTINCT FROM OLD.file_size
        OR NEW.checksum_sha256 IS DISTINCT FROM OLD.checksum_sha256
        OR NEW.description IS DISTINCT FROM OLD.description
        OR NEW.created_at IS DISTINCT FROM OLD.created_at THEN
        RAISE EXCEPTION 'repair_order_attachment content is immutable';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_repair_order_attachment_content_immutable
    BEFORE UPDATE ON repair_order_attachment
    FOR EACH ROW
    EXECUTE FUNCTION prevent_repair_order_attachment_content_update();
