CREATE TABLE IF NOT EXISTS business_request (
    id SERIAL PRIMARY KEY,
    requested_by_user_id INTEGER NOT NULL,
    document_type_id INTEGER NOT NULL,
    document_number VARCHAR(50) NOT NULL,
    business_name VARCHAR(150) NOT NULL,
    legal_name VARCHAR(200),
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    city_id INTEGER NOT NULL,
    address VARCHAR(200),
    legal_file VARCHAR(500) NOT NULL,
    status VARCHAR(50) NOT NULL,
    reviewed_by_user_id INTEGER,
    reviewed_at TIMESTAMP,
    rejection_reason TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business_request
    DROP CONSTRAINT IF EXISTS fk_business_request_requested_by_user;

ALTER TABLE business_request
    ADD CONSTRAINT fk_business_request_requested_by_user
        FOREIGN KEY (requested_by_user_id) REFERENCES app_user (id);

ALTER TABLE business_request
    DROP CONSTRAINT IF EXISTS fk_document_type_id_business_request;

ALTER TABLE business_request
    ADD CONSTRAINT fk_document_type_id_business_request
        FOREIGN KEY (document_type_id) REFERENCES document_type (id);

ALTER TABLE business_request
    DROP CONSTRAINT IF EXISTS fk_city_id_business_request;

ALTER TABLE business_request
    ADD CONSTRAINT fk_city_id_business_request
        FOREIGN KEY (city_id) REFERENCES city (id);

ALTER TABLE business_request
    DROP CONSTRAINT IF EXISTS fk_business_request_reviewed_by_user;

ALTER TABLE business_request
    ADD CONSTRAINT fk_business_request_reviewed_by_user
        FOREIGN KEY (reviewed_by_user_id) REFERENCES app_user (id);
