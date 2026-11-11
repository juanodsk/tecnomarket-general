CREATE TABLE IF NOT EXISTS commission_settlement (
    id SERIAL PRIMARY KEY,
    settlement_code VARCHAR(30) UNIQUE,
    business_id INTEGER NOT NULL,
    business_user_id INTEGER NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    orders_count INTEGER NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    notes VARCHAR(500),
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP,
    paid_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_commission_settlement_business
        FOREIGN KEY (business_id) REFERENCES business(id),
    CONSTRAINT fk_commission_settlement_business_user
        FOREIGN KEY (business_user_id) REFERENCES business_user(id),
    CONSTRAINT ck_commission_settlement_period
        CHECK (period_end >= period_start),
    CONSTRAINT ck_commission_settlement_values
        CHECK (orders_count > 0 AND total_amount >= 0.00),
    CONSTRAINT ck_commission_settlement_status
        CHECK (status IN ('DRAFT', 'APPROVED', 'PAID'))
);

ALTER TABLE repair_order_commission
    ADD COLUMN IF NOT EXISTS settlement_id INTEGER;

INSERT INTO commission_settlement (
    settlement_code,
    business_id,
    business_user_id,
    period_start,
    period_end,
    orders_count,
    total_amount,
    status,
    notes,
    generated_at,
    approved_at,
    paid_at,
    active,
    created_at,
    updated_at
)
SELECT
    'LEGACY-' || commission.id,
    commission.business_id,
    commission.business_user_id,
    COALESCE(repair_order.received_at::date, commission.generated_at::date),
    COALESCE(repair_order.received_at::date, commission.generated_at::date),
    1,
    commission.commission_amount,
    CASE
        WHEN commission.status = 'PAID' THEN 'PAID'
        WHEN commission.status = 'APPROVED' THEN 'APPROVED'
        ELSE 'DRAFT'
    END,
    'Liquidacion migrada desde el flujo individual anterior.',
    commission.generated_at,
    commission.approved_at,
    commission.paid_at,
    commission.active,
    commission.created_at,
    commission.updated_at
FROM repair_order_commission commission
JOIN repair_order ON repair_order.id = commission.repair_order_id
WHERE commission.settlement_id IS NULL
  AND NOT EXISTS (
      SELECT 1
      FROM commission_settlement settlement
      WHERE settlement.settlement_code = 'LEGACY-' || commission.id
  );

UPDATE repair_order_commission commission
SET settlement_id = settlement.id
FROM commission_settlement settlement
WHERE commission.settlement_id IS NULL
  AND settlement.settlement_code = 'LEGACY-' || commission.id;

ALTER TABLE repair_order_commission
    DROP CONSTRAINT IF EXISTS fk_repair_order_commission_settlement;

ALTER TABLE repair_order_commission
    ADD CONSTRAINT fk_repair_order_commission_settlement
        FOREIGN KEY (settlement_id) REFERENCES commission_settlement(id);

ALTER TABLE repair_order_commission
    ALTER COLUMN settlement_id SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_commission_settlement_business
    ON commission_settlement(business_id);

CREATE INDEX IF NOT EXISTS idx_commission_settlement_business_user
    ON commission_settlement(business_id, business_user_id);

CREATE INDEX IF NOT EXISTS idx_commission_settlement_status
    ON commission_settlement(business_id, status);

CREATE INDEX IF NOT EXISTS idx_repair_order_commission_settlement
    ON repair_order_commission(settlement_id);
