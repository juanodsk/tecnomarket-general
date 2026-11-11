# Flyway Migration Flow (TecMarket Backend)

This project uses Flyway as the single source of truth for schema changes.

## Current setup

- `spring.jpa.hibernate.ddl-auto=validate`
- `spring.flyway.enabled=true`
- `spring.flyway.locations=classpath:db/migration`

Flyway scans `db/migration` recursively, so module folders are valid.

## Module folder convention

- `src/main/resources/db/migration/auth`
- `src/main/resources/db/migration/permission`
- `src/main/resources/db/migration/parameter`

## Naming convention

Use:

`V<global_version>__<module>_<short_description>.sql`

Examples:

- `V6__auth_add_password_reset_token.sql`
- `V7__permission_add_business_scope_filter.sql`
- `V8__parameter_seed_new_catalog_value.sql`

## Recommended day-to-day flow

1. Define the DB change first (table, column, index, FK, data fix).
2. Create a new migration SQL file in the corresponding module folder.
3. Keep each migration small and focused.
4. Start the app and let Flyway apply the migration automatically.
5. Validate startup with `ddl-auto=validate`.
6. Commit SQL migration and code changes together.

## Important rules

1. Never modify an already applied migration in shared environments.
2. If a change is needed, create a new migration version.
3. Use idempotent defensive SQL when possible (`IF EXISTS`, `IF NOT EXISTS`).
4. Keep data fixes explicit and auditable.

## Current baseline and release head

- `V1__baseline_existing_schema.sql` marks the imported schema baseline.
- The current schema head is `V50__quick_repair_order_device_identity.sql`.
- Versions are global even when migrations live in module folders.
- CI must migrate a real PostgreSQL database from an empty schema and from the
  previous release target before an image may be published.

## Security and MVP migrations (V38-V50)

| Version | Purpose |
|---------|---------|
| V38 | Store refresh tokens as SHA-256 hashes and add rotation families without truncating sessions. |
| V39 | Add explicit `GLOBAL`/`BUSINESS` permission scope and the financial repair-order permission. |
| V40 | Convert email-verification tokens to one-time hashes. |
| V41 | Separate inventory and sale cost visibility from ordinary read permissions. |
| V42 | Enforce tenant/branch composite foreign keys and financial/quantity checks. |
| V43 | Add session device, IP and last-use metadata. |
| V44 | Add one-time hashed password-reset tokens. |
| V45 | Add tenant-scoped idempotency keys to sales, repair orders and stock movements. |
| V46 | Add user security versions and bind refresh tokens to the version under which they were issued. |
| V47 | Add private evidence integrity metadata, managed public asset keys and a durable object-deletion outbox. |
| V48 | Add repair-part idempotency and enforce append-only repair-order history. |
| V49 | Complete tenant-aware foreign keys, arithmetic checks and pagination indexes for the MVP contract. |
| V50 | Enforce one active IMEI per business for atomic quick repair-order intake. |

V38 migrates existing refresh tokens in place: it hashes their values and assigns
rotation families. It does not use `TRUNCATE` or delete active sessions. No migration
after V38 implicitly truncates operational or authentication tables.

Do not edit these migrations after they have reached a shared environment. Any
correction must use the next global version.
