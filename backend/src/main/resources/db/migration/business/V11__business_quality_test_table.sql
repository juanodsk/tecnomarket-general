CREATE TABLE IF NOT EXISTS business_quality_test (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    device_type_id INTEGER NOT NULL,
    code VARCHAR(30) NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    required BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business_quality_test
    DROP CONSTRAINT IF EXISTS fk_business_id_business_quality_test;

ALTER TABLE business_quality_test
    ADD CONSTRAINT fk_business_id_business_quality_test
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE business_quality_test
    DROP CONSTRAINT IF EXISTS fk_device_type_id_business_quality_test;

ALTER TABLE business_quality_test
    ADD CONSTRAINT fk_device_type_id_business_quality_test
        FOREIGN KEY (device_type_id) REFERENCES device_type (id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_quality_test_code_active
    ON business_quality_test (business_id, device_type_id, code)
    WHERE deleted_at IS NULL;
