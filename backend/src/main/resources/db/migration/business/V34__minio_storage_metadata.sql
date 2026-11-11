ALTER TABLE business_service
    ADD COLUMN IF NOT EXISTS image_url VARCHAR(1000),
    ADD COLUMN IF NOT EXISTS image_object_key VARCHAR(700);

ALTER TABLE repair_order_attachment
    ADD COLUMN IF NOT EXISTS bucket_name VARCHAR(100),
    ADD COLUMN IF NOT EXISTS object_key VARCHAR(700),
    ADD COLUMN IF NOT EXISTS original_file_name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS content_type VARCHAR(100),
    ADD COLUMN IF NOT EXISTS file_size BIGINT;

ALTER TABLE repair_order_attachment
    ALTER COLUMN file_url DROP NOT NULL;

CREATE INDEX IF NOT EXISTS idx_repair_order_attachment_object
    ON repair_order_attachment (bucket_name, object_key)
    WHERE object_key IS NOT NULL;
