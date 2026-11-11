CREATE TABLE IF NOT EXISTS customer_device (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    device_type_id INTEGER NOT NULL,
    brand_id INTEGER NOT NULL,
    model VARCHAR(50) NOT NULL,
    serial_number VARCHAR(60),
    imei VARCHAR(60),
    registered_by_business_user_id INTEGER NOT NULL,
    color VARCHAR(20) NOT NULL,
    registered_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE customer_device
    DROP CONSTRAINT IF EXISTS fk_business_id_customer_device;

ALTER TABLE customer_device
    ADD CONSTRAINT fk_business_id_customer_device
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE customer_device
    DROP CONSTRAINT IF EXISTS fk_user_id_customer_device;

ALTER TABLE customer_device
    ADD CONSTRAINT fk_user_id_customer_device
        FOREIGN KEY (customer_id) REFERENCES app_user (id);

ALTER TABLE customer_device
    DROP CONSTRAINT IF EXISTS fk_device_type_id_customer_device;

ALTER TABLE customer_device
    ADD CONSTRAINT fk_device_type_id_customer_device
        FOREIGN KEY (device_type_id) REFERENCES device_type (id);

ALTER TABLE customer_device
    DROP CONSTRAINT IF EXISTS fk_brand_id_customer_device;

ALTER TABLE customer_device
    ADD CONSTRAINT fk_brand_id_customer_device
        FOREIGN KEY (brand_id) REFERENCES brand (id);

ALTER TABLE customer_device
    DROP CONSTRAINT IF EXISTS fk_business_user_id_customer_device;

ALTER TABLE customer_device
    ADD CONSTRAINT fk_business_user_id_customer_device
        FOREIGN KEY (registered_by_business_user_id) REFERENCES business_user (id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_customer_device_business_customer_imei_active
    ON customer_device (business_id, customer_id, imei)
    WHERE deleted_at IS NULL AND imei IS NOT NULL;
