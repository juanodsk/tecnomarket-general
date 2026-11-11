CREATE TABLE IF NOT EXISTS repair_order_attachment (
    id SERIAL PRIMARY KEY,
    repair_order_id INTEGER NOT NULL,
    uploaded_by_business_user_id INTEGER NOT NULL,
    file_url VARCHAR(1000) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE repair_order_attachment
    DROP CONSTRAINT IF EXISTS fk_repair_order_id_repair_order_attachment;

ALTER TABLE repair_order_attachment
    ADD CONSTRAINT fk_repair_order_id_repair_order_attachment
        FOREIGN KEY (repair_order_id) REFERENCES repair_order (id);

ALTER TABLE repair_order_attachment
    DROP CONSTRAINT IF EXISTS fk_business_user_id_repair_order_attachment;

ALTER TABLE repair_order_attachment
    ADD CONSTRAINT fk_business_user_id_repair_order_attachment
        FOREIGN KEY (uploaded_by_business_user_id) REFERENCES business_user (id);

CREATE INDEX IF NOT EXISTS idx_repair_order_attachment_order_created
    ON repair_order_attachment (repair_order_id, created_at DESC, id DESC);
