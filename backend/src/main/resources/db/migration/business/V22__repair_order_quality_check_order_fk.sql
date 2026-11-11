ALTER TABLE repair_order_quality_check
    DROP CONSTRAINT IF EXISTS fk_repair_order_id_repair_order_quality_check;

ALTER TABLE repair_order_quality_check
    ADD CONSTRAINT fk_repair_order_id_repair_order_quality_check
        FOREIGN KEY (repair_order_id) REFERENCES repair_order (id);

CREATE INDEX IF NOT EXISTS idx_quality_check_business_repair_order
    ON repair_order_quality_check (business_id, repair_order_id, created_at DESC, id DESC);

CREATE UNIQUE INDEX IF NOT EXISTS uq_quality_check_repair_order_test_active
    ON repair_order_quality_check (business_id, repair_order_id, business_quality_test_id)
    WHERE deleted_at IS NULL
      AND repair_order_id IS NOT NULL;
