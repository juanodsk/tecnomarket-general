ALTER TABLE sale ADD COLUMN IF NOT EXISTS idempotency_key VARCHAR(100);
ALTER TABLE sale ADD COLUMN IF NOT EXISTS idempotency_fingerprint VARCHAR(64);
ALTER TABLE repair_order ADD COLUMN IF NOT EXISTS idempotency_key VARCHAR(100);
ALTER TABLE repair_order ADD COLUMN IF NOT EXISTS idempotency_fingerprint VARCHAR(64);
ALTER TABLE stock_movement ADD COLUMN IF NOT EXISTS idempotency_key VARCHAR(100);
ALTER TABLE stock_movement ADD COLUMN IF NOT EXISTS idempotency_fingerprint VARCHAR(64);

CREATE UNIQUE INDEX uq_sale_business_idempotency
    ON sale (business_id, idempotency_key)
    WHERE idempotency_key IS NOT NULL;
CREATE UNIQUE INDEX uq_repair_order_business_idempotency
    ON repair_order (business_id, idempotency_key)
    WHERE idempotency_key IS NOT NULL;
CREATE UNIQUE INDEX uq_stock_movement_branch_idempotency
    ON stock_movement (branch_id, idempotency_key)
    WHERE idempotency_key IS NOT NULL;
