CREATE TABLE IF NOT EXISTS business_schedule (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    day_of_week INTEGER NOT NULL,
    open_time TIME NOT NULL,
    close_time TIME NOT NULL,
    slot_order INTEGER,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business_schedule
    DROP CONSTRAINT IF EXISTS fk_business_id_business_schedule;

ALTER TABLE business_schedule
    ADD CONSTRAINT fk_business_id_business_schedule
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE business_schedule
    DROP CONSTRAINT IF EXISTS ck_business_schedule_day_of_week;

ALTER TABLE business_schedule
    ADD CONSTRAINT ck_business_schedule_day_of_week
        CHECK (day_of_week BETWEEN 1 AND 7);

ALTER TABLE business_schedule
    DROP CONSTRAINT IF EXISTS ck_business_schedule_time_range;

ALTER TABLE business_schedule
    ADD CONSTRAINT ck_business_schedule_time_range
        CHECK (open_time < close_time);

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_schedule_slot_active
    ON business_schedule (business_id, day_of_week, slot_order)
    WHERE deleted_at IS NULL;
