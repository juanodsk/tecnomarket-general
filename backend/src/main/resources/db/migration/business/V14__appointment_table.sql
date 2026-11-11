CREATE TABLE IF NOT EXISTS appointment (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    customer_device_id INTEGER NOT NULL,
    business_service_id INTEGER NOT NULL,
    status_id INTEGER NOT NULL,
    scheduled_at TIMESTAMP NOT NULL,
    problem_description VARCHAR(200) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE appointment
    DROP CONSTRAINT IF EXISTS fk_business_id_appointment;

ALTER TABLE appointment
    ADD CONSTRAINT fk_business_id_appointment
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE appointment
    DROP CONSTRAINT IF EXISTS fk_app_user_id_appointment;

ALTER TABLE appointment
    ADD CONSTRAINT fk_app_user_id_appointment
        FOREIGN KEY (customer_id) REFERENCES app_user (id);

ALTER TABLE appointment
    DROP CONSTRAINT IF EXISTS fk_customer_device_id_appointment;

ALTER TABLE appointment
    ADD CONSTRAINT fk_customer_device_id_appointment
        FOREIGN KEY (customer_device_id) REFERENCES customer_device (id);

ALTER TABLE appointment
    DROP CONSTRAINT IF EXISTS fk_business_service_id_appointment;

ALTER TABLE appointment
    ADD CONSTRAINT fk_business_service_id_appointment
        FOREIGN KEY (business_service_id) REFERENCES business_service (id);

ALTER TABLE appointment
    DROP CONSTRAINT IF EXISTS fk_appointment_status_id_appointment;

ALTER TABLE appointment
    ADD CONSTRAINT fk_appointment_status_id_appointment
        FOREIGN KEY (status_id) REFERENCES appointment_status (id);

CREATE INDEX IF NOT EXISTS idx_appointment_business_scheduled_at
    ON appointment (business_id, scheduled_at);
