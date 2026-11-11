CREATE TABLE IF NOT EXISTS business_review (
    id SERIAL PRIMARY KEY,
    business_id INTEGER NOT NULL,
    customer_id INTEGER NOT NULL,
    repair_order_id INTEGER NOT NULL,
    rating INTEGER NOT NULL,
    comment VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE business_review
    DROP CONSTRAINT IF EXISTS fk_business_id_business_review;

ALTER TABLE business_review
    ADD CONSTRAINT fk_business_id_business_review
        FOREIGN KEY (business_id) REFERENCES business (id);

ALTER TABLE business_review
    DROP CONSTRAINT IF EXISTS fk_app_user_id_business_review;

ALTER TABLE business_review
    ADD CONSTRAINT fk_app_user_id_business_review
        FOREIGN KEY (customer_id) REFERENCES app_user (id);

ALTER TABLE business_review
    DROP CONSTRAINT IF EXISTS fk_repair_order_id_business_review;

ALTER TABLE business_review
    ADD CONSTRAINT fk_repair_order_id_business_review
        FOREIGN KEY (repair_order_id) REFERENCES repair_order (id);

ALTER TABLE business_review
    DROP CONSTRAINT IF EXISTS ck_business_review_rating;

ALTER TABLE business_review
    ADD CONSTRAINT ck_business_review_rating
        CHECK (rating BETWEEN 1 AND 5);

CREATE UNIQUE INDEX IF NOT EXISTS uq_business_review_repair_order
    ON business_review (repair_order_id)
    WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_business_review_business_created
    ON business_review (business_id, created_at DESC, id DESC);
