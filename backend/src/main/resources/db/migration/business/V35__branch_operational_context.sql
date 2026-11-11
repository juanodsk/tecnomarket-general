ALTER TABLE business_branch
    ADD COLUMN IF NOT EXISTS branch_number INTEGER;

WITH numbered AS (
    SELECT id,
           ROW_NUMBER() OVER (PARTITION BY business_id ORDER BY primary_branch DESC, id ASC) AS branch_number
    FROM business_branch
)
UPDATE business_branch branch
SET branch_number = numbered.branch_number
FROM numbered
WHERE branch.id = numbered.id
  AND branch.branch_number IS NULL;

ALTER TABLE business_branch
    ALTER COLUMN branch_number SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_branch_number_active
    ON business_branch (business_id, branch_number)
    WHERE deleted_at IS NULL;

CREATE TABLE IF NOT EXISTS business_user_branch (
    id SERIAL PRIMARY KEY,
    business_user_id INTEGER NOT NULL,
    branch_id INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_business_user_branch_business_user FOREIGN KEY (business_user_id) REFERENCES business_user (id),
    CONSTRAINT fk_business_user_branch_branch FOREIGN KEY (branch_id) REFERENCES business_branch (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_user_branch_active
    ON business_user_branch (business_user_id, branch_id)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_business_user_branch_access
    ON business_user_branch (branch_id, business_user_id, active)
    WHERE deleted_at IS NULL;

INSERT INTO business_user_branch (business_user_id, branch_id, active, created_at, updated_at)
SELECT bu.id, bb.id, TRUE, NOW(), NOW()
FROM business_user bu
JOIN business_branch bb
  ON bb.business_id = bu.business_id
 AND bb.primary_branch = TRUE
 AND bb.deleted_at IS NULL
WHERE bu.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1
      FROM business_user_branch bub
      WHERE bub.business_user_id = bu.id
        AND bub.branch_id = bb.id
        AND bub.deleted_at IS NULL
  );

ALTER TABLE appointment ADD COLUMN IF NOT EXISTS branch_id INTEGER;
ALTER TABLE repair_order ADD COLUMN IF NOT EXISTS branch_id INTEGER;
ALTER TABLE inventory_item ADD COLUMN IF NOT EXISTS branch_id INTEGER;
ALTER TABLE stock_movement ADD COLUMN IF NOT EXISTS branch_id INTEGER;
ALTER TABLE sale ADD COLUMN IF NOT EXISTS branch_id INTEGER;
ALTER TABLE business_schedule ADD COLUMN IF NOT EXISTS branch_id INTEGER;

UPDATE appointment entity
SET branch_id = branch.id
FROM business_branch branch
WHERE entity.business_id = branch.business_id
  AND branch.primary_branch = TRUE
  AND branch.deleted_at IS NULL
  AND entity.branch_id IS NULL;

UPDATE repair_order entity
SET branch_id = branch.id
FROM business_branch branch
WHERE entity.business_id = branch.business_id
  AND branch.primary_branch = TRUE
  AND branch.deleted_at IS NULL
  AND entity.branch_id IS NULL;

UPDATE inventory_item entity
SET branch_id = branch.id
FROM business_branch branch
WHERE entity.business_id = branch.business_id
  AND branch.primary_branch = TRUE
  AND branch.deleted_at IS NULL
  AND entity.branch_id IS NULL;

UPDATE sale entity
SET branch_id = branch.id
FROM business_branch branch
WHERE entity.business_id = branch.business_id
  AND branch.primary_branch = TRUE
  AND branch.deleted_at IS NULL
  AND entity.branch_id IS NULL;

UPDATE business_schedule entity
SET branch_id = branch.id
FROM business_branch branch
WHERE entity.business_id = branch.business_id
  AND branch.primary_branch = TRUE
  AND branch.deleted_at IS NULL
  AND entity.branch_id IS NULL;

UPDATE stock_movement movement
SET branch_id = item.branch_id
FROM inventory_item item
WHERE movement.inventory_item_id = item.id
  AND movement.branch_id IS NULL;

ALTER TABLE appointment ALTER COLUMN branch_id SET NOT NULL;
ALTER TABLE repair_order ALTER COLUMN branch_id SET NOT NULL;
ALTER TABLE inventory_item ALTER COLUMN branch_id SET NOT NULL;
ALTER TABLE stock_movement ALTER COLUMN branch_id SET NOT NULL;
ALTER TABLE sale ALTER COLUMN branch_id SET NOT NULL;
ALTER TABLE business_schedule ALTER COLUMN branch_id SET NOT NULL;

ALTER TABLE appointment
    ADD CONSTRAINT fk_branch_id_appointment FOREIGN KEY (branch_id) REFERENCES business_branch (id);
ALTER TABLE repair_order
    ADD CONSTRAINT fk_branch_id_repair_order FOREIGN KEY (branch_id) REFERENCES business_branch (id);
ALTER TABLE inventory_item
    ADD CONSTRAINT fk_branch_id_inventory_item FOREIGN KEY (branch_id) REFERENCES business_branch (id);
ALTER TABLE stock_movement
    ADD CONSTRAINT fk_branch_id_stock_movement FOREIGN KEY (branch_id) REFERENCES business_branch (id);
ALTER TABLE sale
    ADD CONSTRAINT fk_branch_id_sale FOREIGN KEY (branch_id) REFERENCES business_branch (id);
ALTER TABLE business_schedule
    ADD CONSTRAINT fk_branch_id_business_schedule FOREIGN KEY (branch_id) REFERENCES business_branch (id);

DROP INDEX IF EXISTS uq_repair_order_business_sequence;

ALTER TABLE repair_order_sequence
    DROP CONSTRAINT IF EXISTS repair_order_sequence_pkey;

ALTER TABLE repair_order_sequence
    ADD COLUMN IF NOT EXISTS branch_id INTEGER;

UPDATE repair_order_sequence sequence
SET branch_id = branch.id
FROM business_branch branch
WHERE sequence.business_id = branch.business_id
  AND branch.primary_branch = TRUE
  AND branch.deleted_at IS NULL
  AND sequence.branch_id IS NULL;

ALTER TABLE repair_order_sequence ALTER COLUMN branch_id SET NOT NULL;

ALTER TABLE repair_order_sequence
    ADD CONSTRAINT repair_order_sequence_pkey PRIMARY KEY (business_id, branch_id);

ALTER TABLE repair_order_sequence
    ADD CONSTRAINT fk_repair_order_sequence_branch FOREIGN KEY (branch_id) REFERENCES business_branch (id);

UPDATE repair_order order_entity
SET order_number = 'OS-'
    || LPAD(order_entity.business_id::TEXT, 3, '0')
    || '-'
    || LPAD(branch.branch_number::TEXT, 2, '0')
    || '-'
    || LPAD(order_entity.order_sequence::TEXT, 6, '0')
FROM business_branch branch
WHERE order_entity.branch_id = branch.id;

CREATE UNIQUE INDEX IF NOT EXISTS uq_repair_order_branch_sequence
    ON repair_order (branch_id, order_sequence)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_repair_order_business_branch
    ON repair_order (business_id, branch_id, received_at DESC)
    WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_appointment_business_branch
    ON appointment (business_id, branch_id, scheduled_at DESC)
    WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_inventory_item_business_branch
    ON inventory_item (business_id, branch_id, name)
    WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_sale_business_branch
    ON sale (business_id, branch_id, created_at DESC)
    WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_business_schedule_branch
    ON business_schedule (business_id, branch_id, day_of_week)
    WHERE deleted_at IS NULL;

DROP INDEX IF EXISTS uq_inventory_item_business_sku;
CREATE UNIQUE INDEX IF NOT EXISTS uq_inventory_item_branch_sku
    ON inventory_item (branch_id, sku)
    WHERE deleted_at IS NULL;

DROP INDEX IF EXISTS uq_business_schedule_slot_active;
CREATE UNIQUE INDEX IF NOT EXISTS uq_business_schedule_branch_slot_active
    ON business_schedule (branch_id, day_of_week, slot_order)
    WHERE deleted_at IS NULL;
