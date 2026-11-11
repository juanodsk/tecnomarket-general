CREATE TABLE IF NOT EXISTS business_service (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    service_category_id INTEGER NOT NULL,
    device_type_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    base_price DECIMAL(12,2) NOT NULL,
    estimated_time_minutes INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business_service
    DROP CONSTRAINT IF EXISTS fk_business_id_business_service;

ALTER TABLE business_service
    ADD CONSTRAINT fk_business_id_business_service
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE business_service
    DROP CONSTRAINT IF EXISTS fk_service_category_id_business_service;

ALTER TABLE business_service
    ADD CONSTRAINT fk_service_category_id_business_service
        FOREIGN KEY (service_category_id) REFERENCES service_category (id);

ALTER TABLE business_service
    DROP CONSTRAINT IF EXISTS fk_device_type_id_business_service;

ALTER TABLE business_service
    ADD CONSTRAINT fk_device_type_id_business_service
        FOREIGN KEY (device_type_id) REFERENCES device_type (id);
