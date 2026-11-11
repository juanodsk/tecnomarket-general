CREATE TABLE IF NOT EXISTS repair_order_part (
    id SERIAL PRIMARY KEY,
    repair_order_id INTEGER NOT NULL,
    inventory_item_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    used_by_business_user_id INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE repair_order_part
    DROP CONSTRAINT IF EXISTS fk_repair_order_id_repair_order_part;

ALTER TABLE repair_order_part
    ADD CONSTRAINT fk_repair_order_id_repair_order_part
        FOREIGN KEY (repair_order_id) REFERENCES repair_order (id);

ALTER TABLE repair_order_part
    DROP CONSTRAINT IF EXISTS fk_business_user_id_repair_order_part;

ALTER TABLE repair_order_part
    ADD CONSTRAINT fk_business_user_id_repair_order_part
        FOREIGN KEY (used_by_business_user_id) REFERENCES business_user (id);

CREATE INDEX IF NOT EXISTS idx_repair_order_part_order_created
    ON repair_order_part (repair_order_id, created_at DESC, id DESC);
