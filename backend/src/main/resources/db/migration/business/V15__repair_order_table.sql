CREATE TABLE IF NOT EXISTS repair_order_sequence (
    business_id INTEGER PRIMARY KEY,
    next_sequence INTEGER NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

ALTER TABLE repair_order_sequence
    DROP CONSTRAINT IF EXISTS fk_repair_order_sequence_business;

ALTER TABLE repair_order_sequence
    ADD CONSTRAINT fk_repair_order_sequence_business
        FOREIGN KEY (business_id) REFERENCES business (id);

CREATE TABLE IF NOT EXISTS repair_order (
    id SERIAL PRIMARY KEY,
    order_number VARCHAR(20) NOT NULL,
    order_sequence INTEGER NOT NULL,
    business_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    customer_device_id INTEGER NOT NULL,
    appointment_id INTEGER,
    business_service_id INTEGER NOT NULL,
    status_id INTEGER NOT NULL,
    received_by_business_user_id INTEGER NOT NULL,
    assigned_business_user_id INTEGER NOT NULL,
    problem_description TEXT NOT NULL,
    diagnosis TEXT,
    estimated_price DECIMAL(12,2),
    final_price DECIMAL(12,2),
    received_at TIMESTAMP,
    estimated_delivery_at TIMESTAMP,
    delivered_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE repair_order
    DROP CONSTRAINT IF EXISTS fk_business_id_repair_order;

ALTER TABLE repair_order
    ADD CONSTRAINT fk_business_id_repair_order
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE repair_order
    DROP CONSTRAINT IF EXISTS fk_app_user_id_repair_order;

ALTER TABLE repair_order
    ADD CONSTRAINT fk_app_user_id_repair_order
        FOREIGN KEY (customer_id) REFERENCES app_user (id);

ALTER TABLE repair_order
    DROP CONSTRAINT IF EXISTS fk_customer_device_id_repair_order;

ALTER TABLE repair_order
    ADD CONSTRAINT fk_customer_device_id_repair_order
        FOREIGN KEY (customer_device_id) REFERENCES customer_device (id);

ALTER TABLE repair_order
    DROP CONSTRAINT IF EXISTS fk_appointment_id_repair_order;

ALTER TABLE repair_order
    ADD CONSTRAINT fk_appointment_id_repair_order
        FOREIGN KEY (appointment_id) REFERENCES appointment (id);

ALTER TABLE repair_order
    DROP CONSTRAINT IF EXISTS fk_business_service_id_repair_order;

ALTER TABLE repair_order
    ADD CONSTRAINT fk_business_service_id_repair_order
        FOREIGN KEY (business_service_id) REFERENCES business_service (id);

ALTER TABLE repair_order
    DROP CONSTRAINT IF EXISTS fk_repair_order_status_id_repair_order;

ALTER TABLE repair_order
    ADD CONSTRAINT fk_repair_order_status_id_repair_order
        FOREIGN KEY (status_id) REFERENCES repair_order_status (id);

ALTER TABLE repair_order
    DROP CONSTRAINT IF EXISTS fk_repair_order_received_by_business_user;

ALTER TABLE repair_order
    ADD CONSTRAINT fk_repair_order_received_by_business_user
        FOREIGN KEY (received_by_business_user_id) REFERENCES business_user (id);

ALTER TABLE repair_order
    DROP CONSTRAINT IF EXISTS fk_repair_order_assigned_business_user;

ALTER TABLE repair_order
    ADD CONSTRAINT fk_repair_order_assigned_business_user
        FOREIGN KEY (assigned_business_user_id) REFERENCES business_user (id);

CREATE UNIQUE INDEX IF NOT EXISTS uq_repair_order_business_number
    ON repair_order (business_id, order_number)
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_repair_order_business_sequence
    ON repair_order (business_id, order_sequence)
    WHERE deleted_at IS NULL;
