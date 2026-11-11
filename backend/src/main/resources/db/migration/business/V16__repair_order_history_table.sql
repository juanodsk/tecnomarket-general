CREATE TABLE IF NOT EXISTS repair_order_history (
    id SERIAL PRIMARY KEY,
    repair_order_id INTEGER NOT NULL,
    status_id INTEGER NOT NULL,
    created_by_user_id INTEGER NOT NULL,
    comment TEXT,
    visibility VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

ALTER TABLE repair_order_history
    DROP CONSTRAINT IF EXISTS fk_repair_order_id_repair_order_history;

ALTER TABLE repair_order_history
    ADD CONSTRAINT fk_repair_order_id_repair_order_history
        FOREIGN KEY (repair_order_id) REFERENCES repair_order (id);

ALTER TABLE repair_order_history
    DROP CONSTRAINT IF EXISTS fk_repair_order_status_id_repair_order_history;

ALTER TABLE repair_order_history
    ADD CONSTRAINT fk_repair_order_status_id_repair_order_history
        FOREIGN KEY (status_id) REFERENCES repair_order_status (id);

ALTER TABLE repair_order_history
    DROP CONSTRAINT IF EXISTS fk_app_user_id_repair_order_history;

ALTER TABLE repair_order_history
    ADD CONSTRAINT fk_app_user_id_repair_order_history
        FOREIGN KEY (created_by_user_id) REFERENCES app_user (id);

ALTER TABLE repair_order_history
    DROP CONSTRAINT IF EXISTS ck_repair_order_history_visibility;

ALTER TABLE repair_order_history
    ADD CONSTRAINT ck_repair_order_history_visibility
        CHECK (visibility IN ('INTERNAL', 'CUSTOMER'));

CREATE INDEX IF NOT EXISTS idx_repair_order_history_order_created
    ON repair_order_history (repair_order_id, created_at DESC, id DESC);
