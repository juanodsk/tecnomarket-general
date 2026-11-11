CREATE TABLE IF NOT EXISTS quotation (
    id SERIAL PRIMARY KEY,
    repair_order_id INTEGER NOT NULL,
    business_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    quotation_status_id INTEGER NOT NULL,
    notes VARCHAR(500),
    expires_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE quotation
    DROP CONSTRAINT IF EXISTS fk_repair_order_id_quotation;

ALTER TABLE quotation
    ADD CONSTRAINT fk_repair_order_id_quotation
        FOREIGN KEY (repair_order_id) REFERENCES repair_order (id);

ALTER TABLE quotation
    DROP CONSTRAINT IF EXISTS fk_business_id_quotation;

ALTER TABLE quotation
    ADD CONSTRAINT fk_business_id_quotation
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE quotation
    DROP CONSTRAINT IF EXISTS fk_customer_id_quotation;

ALTER TABLE quotation
    ADD CONSTRAINT fk_customer_id_quotation
        FOREIGN KEY (customer_id) REFERENCES app_user (id);

ALTER TABLE quotation
    DROP CONSTRAINT IF EXISTS fk_quotation_status_id_quotation;

ALTER TABLE quotation
    ADD CONSTRAINT fk_quotation_status_id_quotation
        FOREIGN KEY (quotation_status_id) REFERENCES quotation_status (id);

CREATE INDEX IF NOT EXISTS idx_quotation_business_created
    ON quotation (business_id, created_at DESC, id DESC);

CREATE INDEX IF NOT EXISTS idx_quotation_repair_order
    ON quotation (repair_order_id, created_at DESC, id DESC);

CREATE TABLE IF NOT EXISTS quotation_item (
    id SERIAL PRIMARY KEY,
    quotation_id INTEGER NOT NULL,
    description VARCHAR(300) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE quotation_item
    DROP CONSTRAINT IF EXISTS fk_quotation_id_quotation_item;

ALTER TABLE quotation_item
    ADD CONSTRAINT fk_quotation_id_quotation_item
        FOREIGN KEY (quotation_id) REFERENCES quotation (id);

CREATE INDEX IF NOT EXISTS idx_quotation_item_quotation
    ON quotation_item (quotation_id, created_at ASC, id ASC);
