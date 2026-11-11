UPDATE customer_device
SET imei = NULL
WHERE imei IS NOT NULL
  AND BTRIM(imei) = '';

UPDATE customer_device
SET imei = BTRIM(imei)
WHERE imei IS NOT NULL
  AND imei <> BTRIM(imei);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM customer_device
        WHERE deleted_at IS NULL
          AND imei IS NOT NULL
          AND BTRIM(imei) <> ''
        GROUP BY business_id, imei
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION 'Cannot enforce business-wide IMEI uniqueness: duplicate active customer_device rows exist';
    END IF;
END $$;

DROP INDEX IF EXISTS uq_customer_device_business_customer_imei_active;

CREATE UNIQUE INDEX uq_customer_device_business_imei_active
    ON customer_device (business_id, imei)
    WHERE deleted_at IS NULL
      AND imei IS NOT NULL
      AND BTRIM(imei) <> '';
