CREATE TABLE IF NOT EXISTS business_catalog_item (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    catalog_type VARCHAR(40) NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    icon VARCHAR(255),
    sort_order INTEGER,
    protected_item BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business_catalog_item
    DROP CONSTRAINT IF EXISTS fk_business_id_business_catalog_item;

ALTER TABLE business_catalog_item
    ADD CONSTRAINT fk_business_id_business_catalog_item
        FOREIGN KEY (business_id) REFERENCES business (id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_catalog_item_code_active
    ON business_catalog_item (business_id, catalog_type, code)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_business_catalog_item_business_type
    ON business_catalog_item (business_id, catalog_type, active, sort_order, name);

INSERT INTO business_catalog_item (business_id, catalog_type, code, name, description, icon, sort_order, protected_item, active, created_at, updated_at)
SELECT b.id, 'SERVICE_CATEGORY', UPPER(REPLACE(sc.code, '-', '_')), sc.name, sc.description, sc.icon, sc.sort_order, FALSE, sc.active, NOW(), NOW()
FROM business b
CROSS JOIN service_category sc
WHERE b.deleted_at IS NULL
  AND sc.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM business_catalog_item item
      WHERE item.business_id = b.id
        AND item.catalog_type = 'SERVICE_CATEGORY'
        AND item.code = UPPER(REPLACE(sc.code, '-', '_'))
        AND item.deleted_at IS NULL
  );

INSERT INTO business_catalog_item (business_id, catalog_type, code, name, description, protected_item, active, created_at, updated_at)
SELECT b.id, 'DEVICE_TYPE', UPPER(REPLACE(dt.code, '-', '_')), dt.name, dt.description, FALSE, dt.active, NOW(), NOW()
FROM business b
CROSS JOIN device_type dt
WHERE b.deleted_at IS NULL
  AND dt.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM business_catalog_item item
      WHERE item.business_id = b.id
        AND item.catalog_type = 'DEVICE_TYPE'
        AND item.code = UPPER(REPLACE(dt.code, '-', '_'))
        AND item.deleted_at IS NULL
  );

INSERT INTO business_catalog_item (business_id, catalog_type, code, name, protected_item, active, created_at, updated_at)
SELECT b.id,
       'BRAND',
       UPPER(REGEXP_REPLACE(REGEXP_REPLACE(br.name, '[^a-zA-Z0-9]+', '_', 'g'), '(^_|_$)', '', 'g')),
       br.name,
       FALSE,
       br.active,
       NOW(),
       NOW()
FROM business b
CROSS JOIN brand br
WHERE b.deleted_at IS NULL
  AND br.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM business_catalog_item item
      WHERE item.business_id = b.id
        AND item.catalog_type = 'BRAND'
        AND item.code = UPPER(REGEXP_REPLACE(REGEXP_REPLACE(br.name, '[^a-zA-Z0-9]+', '_', 'g'), '(^_|_$)', '', 'g'))
        AND item.deleted_at IS NULL
  );

INSERT INTO business_catalog_item (business_id, catalog_type, code, name, description, protected_item, active, created_at, updated_at)
SELECT b.id, 'APPOINTMENT_STATUS', UPPER(REPLACE(ast.code, '-', '_')), ast.name, ast.description, TRUE, ast.active, NOW(), NOW()
FROM business b
CROSS JOIN appointment_status ast
WHERE b.deleted_at IS NULL
  AND ast.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM business_catalog_item item
      WHERE item.business_id = b.id
        AND item.catalog_type = 'APPOINTMENT_STATUS'
        AND item.code = UPPER(REPLACE(ast.code, '-', '_'))
        AND item.deleted_at IS NULL
  );

INSERT INTO business_catalog_item (business_id, catalog_type, code, name, description, sort_order, protected_item, active, created_at, updated_at)
SELECT b.id, 'REPAIR_ORDER_STATUS', UPPER(REPLACE(ros.code, '-', '_')), ros.name, ros.description, ros.sort_order, TRUE, ros.active, NOW(), NOW()
FROM business b
CROSS JOIN repair_order_status ros
WHERE b.deleted_at IS NULL
  AND ros.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM business_catalog_item item
      WHERE item.business_id = b.id
        AND item.catalog_type = 'REPAIR_ORDER_STATUS'
        AND item.code = UPPER(REPLACE(ros.code, '-', '_'))
        AND item.deleted_at IS NULL
  );

INSERT INTO business_catalog_item (business_id, catalog_type, code, name, description, protected_item, active, created_at, updated_at)
SELECT b.id, 'QUOTATION_STATUS', UPPER(REPLACE(qs.code, '-', '_')), qs.name, qs.description, TRUE, qs.active, NOW(), NOW()
FROM business b
CROSS JOIN quotation_status qs
WHERE b.deleted_at IS NULL
  AND qs.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM business_catalog_item item
      WHERE item.business_id = b.id
        AND item.catalog_type = 'QUOTATION_STATUS'
        AND item.code = UPPER(REPLACE(qs.code, '-', '_'))
        AND item.deleted_at IS NULL
  );

INSERT INTO business_catalog_item (business_id, catalog_type, code, name, description, protected_item, active, created_at, updated_at)
SELECT b.id, 'ITEM_CATEGORY', UPPER(REPLACE(ic.code, '-', '_')), ic.name, ic.description, FALSE, ic.active, NOW(), NOW()
FROM business b
CROSS JOIN item_category ic
WHERE b.deleted_at IS NULL
  AND ic.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM business_catalog_item item
      WHERE item.business_id = b.id
        AND item.catalog_type = 'ITEM_CATEGORY'
        AND item.code = UPPER(REPLACE(ic.code, '-', '_'))
        AND item.deleted_at IS NULL
  );

INSERT INTO business_catalog_item (business_id, catalog_type, code, name, description, protected_item, active, created_at, updated_at)
SELECT b.id, 'MOVEMENT_TYPE', UPPER(REPLACE(mt.code, '-', '_')), mt.name, mt.description, TRUE, mt.active, NOW(), NOW()
FROM business b
CROSS JOIN movement_type mt
WHERE b.deleted_at IS NULL
  AND mt.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM business_catalog_item item
      WHERE item.business_id = b.id
        AND item.catalog_type = 'MOVEMENT_TYPE'
        AND item.code = UPPER(REPLACE(mt.code, '-', '_'))
        AND item.deleted_at IS NULL
  );

INSERT INTO business_catalog_item (business_id, catalog_type, code, name, description, protected_item, active, created_at, updated_at)
SELECT b.id, 'PAYMENT_METHOD', UPPER(REPLACE(pm.code, '-', '_')), pm.name, pm.description, FALSE, pm.active, NOW(), NOW()
FROM business b
CROSS JOIN payment_method pm
WHERE b.deleted_at IS NULL
  AND pm.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM business_catalog_item item
      WHERE item.business_id = b.id
        AND item.catalog_type = 'PAYMENT_METHOD'
        AND item.code = UPPER(REPLACE(pm.code, '-', '_'))
        AND item.deleted_at IS NULL
  );
