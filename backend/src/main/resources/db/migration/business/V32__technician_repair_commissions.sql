ALTER TABLE business_user
    ADD COLUMN IF NOT EXISTS commission_percentage NUMERIC(5, 2) NOT NULL DEFAULT 0.00;

ALTER TABLE business_user
    DROP CONSTRAINT IF EXISTS ck_business_user_commission_percentage;

ALTER TABLE business_user
    ADD CONSTRAINT ck_business_user_commission_percentage
        CHECK (commission_percentage >= 0.00 AND commission_percentage <= 100.00);

CREATE TABLE IF NOT EXISTS repair_order_commission (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    repair_order_id INTEGER NOT NULL,
    business_user_id INTEGER NOT NULL,
    order_total NUMERIC(12, 2) NOT NULL,
    parts_total NUMERIC(12, 2) NOT NULL,
    commission_base NUMERIC(12, 2) NOT NULL,
    commission_percentage NUMERIC(5, 2) NOT NULL,
    commission_amount NUMERIC(12, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP,
    paid_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT uq_repair_order_commission_order UNIQUE (repair_order_id),
    CONSTRAINT fk_repair_order_commission_business
        FOREIGN KEY (business_id) REFERENCES business(id),
    CONSTRAINT fk_repair_order_commission_order
        FOREIGN KEY (repair_order_id) REFERENCES repair_order(id),
    CONSTRAINT fk_repair_order_commission_business_user
        FOREIGN KEY (business_user_id) REFERENCES business_user(id),
    CONSTRAINT ck_repair_order_commission_percentage
        CHECK (commission_percentage >= 0.00 AND commission_percentage <= 100.00),
    CONSTRAINT ck_repair_order_commission_values
        CHECK (
            order_total >= 0.00
            AND parts_total >= 0.00
            AND commission_base >= 0.00
            AND commission_amount >= 0.00
            AND order_total >= parts_total
        ),
    CONSTRAINT ck_repair_order_commission_status
        CHECK (status IN ('PENDING', 'APPROVED', 'PAID', 'CANCELLED'))
);

CREATE INDEX IF NOT EXISTS idx_repair_order_commission_business
    ON repair_order_commission(business_id);

CREATE INDEX IF NOT EXISTS idx_repair_order_commission_business_user
    ON repair_order_commission(business_id, business_user_id);

CREATE INDEX IF NOT EXISTS idx_repair_order_commission_status
    ON repair_order_commission(business_id, status);
