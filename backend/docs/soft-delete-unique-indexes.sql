-- TecMarket: soft-delete aware uniqueness
-- Run this once in PostgreSQL to allow reusing code/name when previous row is logically deleted.

BEGIN;

-- =========================
-- Permission / Auth catalogs
-- =========================
ALTER TABLE role DROP CONSTRAINT IF EXISTS uk_role_code;
ALTER TABLE role DROP CONSTRAINT IF EXISTS role_code_key;

ALTER TABLE app_module DROP CONSTRAINT IF EXISTS uk_app_module_code;
ALTER TABLE app_module DROP CONSTRAINT IF EXISTS app_module_code_key;

ALTER TABLE permission DROP CONSTRAINT IF EXISTS uk_permission_code;
ALTER TABLE permission DROP CONSTRAINT IF EXISTS permission_code_key;

ALTER TABLE document_type DROP CONSTRAINT IF EXISTS uk_document_type_code;
ALTER TABLE document_type DROP CONSTRAINT IF EXISTS document_type_code_key;

-- =================
-- Global parameters
-- =================
ALTER TABLE country DROP CONSTRAINT IF EXISTS uk_country_code;
ALTER TABLE country DROP CONSTRAINT IF EXISTS country_code_key;

ALTER TABLE city DROP CONSTRAINT IF EXISTS uk_city_country_name;
ALTER TABLE city DROP CONSTRAINT IF EXISTS city_country_id_name_key;

ALTER TABLE brand DROP CONSTRAINT IF EXISTS uk_brand_name;
ALTER TABLE brand DROP CONSTRAINT IF EXISTS brand_name_key;

ALTER TABLE service_category DROP CONSTRAINT IF EXISTS uk_service_category_code;
ALTER TABLE service_category DROP CONSTRAINT IF EXISTS service_category_code_key;

ALTER TABLE device_type DROP CONSTRAINT IF EXISTS uk_device_type_code;
ALTER TABLE device_type DROP CONSTRAINT IF EXISTS device_type_code_key;

ALTER TABLE appointment_status DROP CONSTRAINT IF EXISTS uk_appointment_status_code;
ALTER TABLE appointment_status DROP CONSTRAINT IF EXISTS appointment_status_code_key;

ALTER TABLE repair_order_status DROP CONSTRAINT IF EXISTS uk_repair_order_status_code;
ALTER TABLE repair_order_status DROP CONSTRAINT IF EXISTS repair_order_status_code_key;

ALTER TABLE quotation_status DROP CONSTRAINT IF EXISTS uk_quotation_status_code;
ALTER TABLE quotation_status DROP CONSTRAINT IF EXISTS quotation_status_code_key;

ALTER TABLE item_category DROP CONSTRAINT IF EXISTS uk_item_category_code;
ALTER TABLE item_category DROP CONSTRAINT IF EXISTS item_category_code_key;

ALTER TABLE movement_type DROP CONSTRAINT IF EXISTS uk_movement_type_code;
ALTER TABLE movement_type DROP CONSTRAINT IF EXISTS movement_type_code_key;

ALTER TABLE payment_method DROP CONSTRAINT IF EXISTS uk_payment_method_code;
ALTER TABLE payment_method DROP CONSTRAINT IF EXISTS payment_method_code_key;

-- ============================================
-- Partial unique indexes (only active records)
-- ============================================
CREATE UNIQUE INDEX IF NOT EXISTS uq_role_code_active
ON role (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_app_module_code_active
ON app_module (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_permission_code_active
ON permission (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_document_type_code_active
ON document_type (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_country_code_active
ON country (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_city_country_name_active
ON city (country_id, lower(name))
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_brand_name_active
ON brand (lower(name))
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_service_category_code_active
ON service_category (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_device_type_code_active
ON device_type (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_appointment_status_code_active
ON appointment_status (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_repair_order_status_code_active
ON repair_order_status (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_quotation_status_code_active
ON quotation_status (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_item_category_code_active
ON item_category (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_movement_type_code_active
ON movement_type (code)
WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_payment_method_code_active
ON payment_method (code)
WHERE deleted_at IS NULL;

COMMIT;
