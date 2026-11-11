CREATE TABLE IF NOT EXISTS repair_order_quality_check (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    repair_order_id INTEGER,
    intake_ticket_code VARCHAR(60) NOT NULL,
    business_quality_test_id INTEGER NOT NULL,
    checked_by_user_id INTEGER NOT NULL,
    result VARCHAR(20) NOT NULL,
    notes VARCHAR(300),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    checked_at TIMESTAMP NOT NULL DEFAULT NOW()
);

ALTER TABLE repair_order_quality_check
    DROP CONSTRAINT IF EXISTS fk_business_id_repair_order_quality_check;

ALTER TABLE repair_order_quality_check
    ADD CONSTRAINT fk_business_id_repair_order_quality_check
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE repair_order_quality_check
    DROP CONSTRAINT IF EXISTS fk_business_quality_test_id_repair_order_quality_check;

ALTER TABLE repair_order_quality_check
    ADD CONSTRAINT fk_business_quality_test_id_repair_order_quality_check
        FOREIGN KEY (business_quality_test_id) REFERENCES business_quality_test (id);

ALTER TABLE repair_order_quality_check
    DROP CONSTRAINT IF EXISTS fk_checked_by_user_id_repair_order_quality_check;

ALTER TABLE repair_order_quality_check
    ADD CONSTRAINT fk_checked_by_user_id_repair_order_quality_check
        FOREIGN KEY (checked_by_user_id) REFERENCES app_user (id);

ALTER TABLE repair_order_quality_check
    DROP CONSTRAINT IF EXISTS ck_repair_order_quality_check_result;

ALTER TABLE repair_order_quality_check
    ADD CONSTRAINT ck_repair_order_quality_check_result
        CHECK (result IN ('SI', 'NO', 'NO_APLICA'));

CREATE UNIQUE INDEX IF NOT EXISTS uq_quality_check_ticket_test_active
    ON repair_order_quality_check (business_id, intake_ticket_code, business_quality_test_id)
    WHERE deleted_at IS NULL;
