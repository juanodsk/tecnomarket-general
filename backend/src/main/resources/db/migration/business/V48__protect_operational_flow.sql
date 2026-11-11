ALTER TABLE repair_order_part
    ADD COLUMN IF NOT EXISTS idempotency_key VARCHAR(100),
    ADD COLUMN IF NOT EXISTS idempotency_fingerprint VARCHAR(64);

CREATE UNIQUE INDEX IF NOT EXISTS uq_repair_order_part_order_idempotency
    ON repair_order_part (repair_order_id, idempotency_key)
    WHERE idempotency_key IS NOT NULL;

CREATE OR REPLACE FUNCTION prevent_repair_order_history_mutation()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    RAISE EXCEPTION 'repair_order_history is append-only';
END;
$$;

DROP TRIGGER IF EXISTS trg_repair_order_history_immutable ON repair_order_history;
CREATE TRIGGER trg_repair_order_history_immutable
    BEFORE UPDATE OR DELETE ON repair_order_history
    FOR EACH ROW
    EXECUTE FUNCTION prevent_repair_order_history_mutation();
