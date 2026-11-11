CREATE TABLE IF NOT EXISTS business (
    id SERIAL PRIMARY KEY,
    document_type_id INTEGER NOT NULL,
    document_number VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    legal_name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    address VARCHAR(200) NOT NULL,
    description VARCHAR(255) NOT NULL,
    city_id INTEGER NOT NULL,
    logo VARCHAR(500),
    cover_image VARCHAR(500),
    rating_average DECIMAL(3,2),
    rating_count INTEGER,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business
    DROP CONSTRAINT IF EXISTS fk_document_type_id_business;

ALTER TABLE business
    ADD CONSTRAINT fk_document_type_id_business FOREIGN KEY (document_type_id) REFERENCES document_type (id);

ALTER TABLE business
    DROP CONSTRAINT IF EXISTS fk_city_id_business;

ALTER TABLE business
    ADD CONSTRAINT fk_city_id_business FOREIGN KEY (city_id) REFERENCES city (id);

CREATE TABLE IF NOT EXISTS business_user (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business_user
    DROP CONSTRAINT IF EXISTS fk_business_id_business_user;

ALTER TABLE business_user
    ADD CONSTRAINT fk_business_id_business_user FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE business_user
    DROP CONSTRAINT IF EXISTS fk_app_user_id_business_user;

ALTER TABLE business_user
    ADD CONSTRAINT fk_app_user_id_business_user FOREIGN KEY (user_id) REFERENCES app_user (id);

ALTER TABLE business_user
    DROP CONSTRAINT IF EXISTS fk_role_id_business_user;

ALTER TABLE business_user
    ADD CONSTRAINT fk_role_id_business_user FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE business_user
    DROP CONSTRAINT IF EXISTS uq_business_user;

ALTER TABLE business_user
    ADD CONSTRAINT uq_business_user UNIQUE (business_id, user_id, role_id);
