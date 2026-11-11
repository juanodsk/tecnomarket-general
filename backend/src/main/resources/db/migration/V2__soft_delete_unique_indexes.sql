-- TecMarket: soft-delete aware uniqueness
-- Safe for existing schemas. Statements are guarded per table.

-- Drop old unique constraints when table exists.
ALTER TABLE IF EXISTS role DROP CONSTRAINT IF EXISTS uk_role_code;
ALTER TABLE IF EXISTS role DROP CONSTRAINT IF EXISTS role_code_key;

ALTER TABLE IF EXISTS app_module DROP CONSTRAINT IF EXISTS uk_app_module_code;
ALTER TABLE IF EXISTS app_module DROP CONSTRAINT IF EXISTS app_module_code_key;

ALTER TABLE IF EXISTS permission DROP CONSTRAINT IF EXISTS uk_permission_code;
ALTER TABLE IF EXISTS permission DROP CONSTRAINT IF EXISTS permission_code_key;

ALTER TABLE IF EXISTS document_type DROP CONSTRAINT IF EXISTS uk_document_type_code;
ALTER TABLE IF EXISTS document_type DROP CONSTRAINT IF EXISTS document_type_code_key;

ALTER TABLE IF EXISTS country DROP CONSTRAINT IF EXISTS uk_country_code;
ALTER TABLE IF EXISTS country DROP CONSTRAINT IF EXISTS country_code_key;

ALTER TABLE IF EXISTS city DROP CONSTRAINT IF EXISTS uk_city_country_name;
ALTER TABLE IF EXISTS city DROP CONSTRAINT IF EXISTS city_country_id_name_key;

ALTER TABLE IF EXISTS brand DROP CONSTRAINT IF EXISTS uk_brand_name;
ALTER TABLE IF EXISTS brand DROP CONSTRAINT IF EXISTS brand_name_key;

ALTER TABLE IF EXISTS service_category DROP CONSTRAINT IF EXISTS uk_service_category_code;
ALTER TABLE IF EXISTS service_category DROP CONSTRAINT IF EXISTS service_category_code_key;

ALTER TABLE IF EXISTS device_type DROP CONSTRAINT IF EXISTS uk_device_type_code;
ALTER TABLE IF EXISTS device_type DROP CONSTRAINT IF EXISTS device_type_code_key;

ALTER TABLE IF EXISTS appointment_status DROP CONSTRAINT IF EXISTS uk_appointment_status_code;
ALTER TABLE IF EXISTS appointment_status DROP CONSTRAINT IF EXISTS appointment_status_code_key;

ALTER TABLE IF EXISTS repair_order_status DROP CONSTRAINT IF EXISTS uk_repair_order_status_code;
ALTER TABLE IF EXISTS repair_order_status DROP CONSTRAINT IF EXISTS repair_order_status_code_key;

ALTER TABLE IF EXISTS quotation_status DROP CONSTRAINT IF EXISTS uk_quotation_status_code;
ALTER TABLE IF EXISTS quotation_status DROP CONSTRAINT IF EXISTS quotation_status_code_key;

ALTER TABLE IF EXISTS item_category DROP CONSTRAINT IF EXISTS uk_item_category_code;
ALTER TABLE IF EXISTS item_category DROP CONSTRAINT IF EXISTS item_category_code_key;

ALTER TABLE IF EXISTS movement_type DROP CONSTRAINT IF EXISTS uk_movement_type_code;
ALTER TABLE IF EXISTS movement_type DROP CONSTRAINT IF EXISTS movement_type_code_key;

ALTER TABLE IF EXISTS payment_method DROP CONSTRAINT IF EXISTS uk_payment_method_code;
ALTER TABLE IF EXISTS payment_method DROP CONSTRAINT IF EXISTS payment_method_code_key;

-- Create partial unique indexes only when table exists.
DO $$
BEGIN
  IF to_regclass('public.role') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_role_code_active ON role (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.app_module') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_app_module_code_active ON app_module (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.permission') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_permission_code_active ON permission (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.document_type') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_document_type_code_active ON document_type (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.country') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_country_code_active ON country (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.city') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_city_country_name_active ON city (country_id, lower(name)) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.brand') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_brand_name_active ON brand (lower(name)) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.service_category') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_service_category_code_active ON service_category (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.device_type') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_device_type_code_active ON device_type (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.appointment_status') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_appointment_status_code_active ON appointment_status (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.repair_order_status') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_repair_order_status_code_active ON repair_order_status (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.quotation_status') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_quotation_status_code_active ON quotation_status (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.item_category') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_item_category_code_active ON item_category (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.movement_type') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_movement_type_code_active ON movement_type (code) WHERE deleted_at IS NULL';
  END IF;
END $$;

DO $$
BEGIN
  IF to_regclass('public.payment_method') IS NOT NULL THEN
    EXECUTE 'CREATE UNIQUE INDEX IF NOT EXISTS uq_payment_method_code_active ON payment_method (code) WHERE deleted_at IS NULL';
  END IF;
END $$;
