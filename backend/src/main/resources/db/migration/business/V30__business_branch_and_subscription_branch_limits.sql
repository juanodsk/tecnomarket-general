ALTER TABLE subscription_plan
    ADD COLUMN IF NOT EXISTS max_branches INTEGER NOT NULL DEFAULT 1;

ALTER TABLE subscription_usage
    ADD COLUMN IF NOT EXISTS branches_count INTEGER NOT NULL DEFAULT 0;

CREATE TABLE IF NOT EXISTS business_branch (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    name VARCHAR(120) NOT NULL,
    city_id INTEGER NOT NULL,
    address VARCHAR(200) NOT NULL,
    neighborhood VARCHAR(120),
    phone VARCHAR(30),
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),
    service_radius_km DECIMAL(6, 2),
    primary_branch BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business_branch
    DROP CONSTRAINT IF EXISTS fk_business_id_business_branch;

ALTER TABLE business_branch
    ADD CONSTRAINT fk_business_id_business_branch
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE business_branch
    DROP CONSTRAINT IF EXISTS fk_city_id_business_branch;

ALTER TABLE business_branch
    ADD CONSTRAINT fk_city_id_business_branch
        FOREIGN KEY (city_id) REFERENCES city (id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_branch_name_active
    ON business_branch (business_id, LOWER(name))
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_branch_primary_active
    ON business_branch (business_id)
    WHERE primary_branch = TRUE AND deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_business_branch_business_active
    ON business_branch (business_id, active)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_business_branch_city_active
    ON business_branch (city_id, active)
    WHERE deleted_at IS NULL;

INSERT INTO business_branch (
    business_id,
    name,
    city_id,
    address,
    phone,
    primary_branch,
    active,
    created_at,
    updated_at
)
SELECT
    b.id,
    'Sede principal',
    b.city_id,
    b.address,
    b.phone,
    TRUE,
    TRUE,
    COALESCE(b.created_at, NOW()),
    NOW()
FROM business b
WHERE b.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1
      FROM business_branch bb
      WHERE bb.business_id = b.id
        AND bb.deleted_at IS NULL
  );
