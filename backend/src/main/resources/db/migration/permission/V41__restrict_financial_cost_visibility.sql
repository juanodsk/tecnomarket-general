INSERT INTO permission (
    module_id, code, name, description, scope, active, created_at, updated_at, deleted_at
)
SELECT module.id, seed_values.code, seed_values.name, seed_values.description, 'BUSINESS', TRUE, NOW(), NOW(), NULL
FROM app_module module
JOIN (VALUES
    ('INVENTORY', 'INVENTORY_COST_VIEW', 'Ver costos de inventario',
     'Permite consultar costos y margenes del inventario.'),
    ('SALE', 'SALE_COST_VIEW', 'Ver costos de ventas',
     'Permite consultar costos y margenes historicos de ventas.')
) AS seed_values(module_code, code, name, description)
    ON seed_values.module_code = module.code
WHERE module.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1 FROM permission current_permission
      WHERE current_permission.code = seed_values.code
        AND current_permission.deleted_at IS NULL
  );

INSERT INTO role_permission (role_id, permission_id, active, created_at, updated_at, deleted_at)
SELECT role.id, permission.id, TRUE, NOW(), NOW(), NULL
FROM role
JOIN permission ON permission.code IN ('INVENTORY_COST_VIEW', 'SALE_COST_VIEW')
WHERE role.code IN ('BUSINESS_OWNER', 'BUSINESS_ADMIN')
  AND role.deleted_at IS NULL
  AND permission.deleted_at IS NULL
ON CONFLICT (role_id, permission_id)
DO UPDATE SET active = TRUE, updated_at = NOW(), deleted_at = NULL;
