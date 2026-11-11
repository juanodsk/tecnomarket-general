ALTER TABLE permission
    ADD COLUMN IF NOT EXISTS scope VARCHAR(20);

UPDATE permission
SET scope = CASE
    WHEN code IN (
        'PARAMETER_VIEW',
        'PARAMETER_MANAGE',
        'BUSINESS_REQUEST_REVIEW',
        'SUBSCRIPTION_VIEW',
        'SUBSCRIPTION_MANAGE'
    ) THEN 'GLOBAL'
    ELSE 'BUSINESS'
END;

ALTER TABLE permission
    ALTER COLUMN scope SET NOT NULL;

ALTER TABLE permission
    DROP CONSTRAINT IF EXISTS chk_permission_scope;

ALTER TABLE permission
    ADD CONSTRAINT chk_permission_scope
        CHECK (scope IN ('GLOBAL', 'BUSINESS'));

INSERT INTO permission (
    module_id,
    code,
    name,
    description,
    scope,
    active,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    module.id,
    'REPAIR_ORDER_FINANCIAL_UPDATE',
    'Actualizar valores de ordenes',
    'Permite actualizar el precio final de una orden de reparacion.',
    'BUSINESS',
    TRUE,
    NOW(),
    NOW(),
    NULL
FROM app_module module
WHERE module.code = 'REPAIR_ORDER'
  AND module.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1
      FROM permission current_permission
      WHERE current_permission.code = 'REPAIR_ORDER_FINANCIAL_UPDATE'
        AND current_permission.deleted_at IS NULL
  );

INSERT INTO role_permission (role_id, permission_id, active, created_at, updated_at, deleted_at)
SELECT role.id, permission.id, TRUE, NOW(), NOW(), NULL
FROM role
JOIN permission ON permission.code = 'REPAIR_ORDER_FINANCIAL_UPDATE'
WHERE role.code IN ('BUSINESS_OWNER', 'BUSINESS_ADMIN', 'RECEPTIONIST')
  AND role.deleted_at IS NULL
  AND permission.deleted_at IS NULL
ON CONFLICT (role_id, permission_id)
DO UPDATE SET active = TRUE, updated_at = NOW(), deleted_at = NULL;

UPDATE role_permission relation
SET active = FALSE,
    updated_at = NOW(),
    deleted_at = COALESCE(relation.deleted_at, NOW())
FROM role, permission
WHERE relation.role_id = role.id
  AND relation.permission_id = permission.id
  AND role.scope = 'BUSINESS'
  AND permission.scope = 'GLOBAL'
  AND relation.deleted_at IS NULL;

UPDATE user_role relation
SET active = FALSE,
    updated_at = NOW(),
    deleted_at = COALESCE(relation.deleted_at, NOW())
FROM role
WHERE relation.role_id = role.id
  AND role.scope = 'BUSINESS'
  AND relation.deleted_at IS NULL;
