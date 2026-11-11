# Validacion Modelo DrawDB vs Entidades JPA

Fecha: 2026-05-15

## Alcance validado

- Modulos con entidades JPA actuales:
  - `user`
  - `permission`
  - `parameter` (country/city)
  - `auth` (email verification token)

## Ajustes aplicados en DrawDB JSON

Se alinearon nombres de FKs del modelo DrawDB con los nombres definidos en `@ForeignKey` de JPA:

1. `fk_country_id_city` -> `fk_city_country`
2. `fk_role_id_user_role` -> `fk_user_role_role`
3. `fk_app_user_id_user_role` -> `fk_user_role_user`
4. `fk_module_id_permission` -> `fk_permission_module`
5. `fk_role_id_role_permission` -> `fk_role_permission_role`
6. `fk_permission_id_role_permission` -> `fk_role_permission_permission`

## Uniques: criterio actual

Las tablas de catalogo y seguridad usan estrategia **soft-delete aware uniqueness** via Flyway (indices parciales con `deleted_at IS NULL`), definida en:

- `src/main/resources/db/migration/V2__soft_delete_unique_indexes.sql`

Por esa razon, varias entidades JPA no declaran `unique=true` directamente en `@Column`/`@Table`, y la fuente de verdad para esos uniques es Flyway.

## Hallazgo pendiente de documentacion

- La entidad JPA `email_verification_token` existe en codigo (`auth/entity/EmailVerificationToken.java`), pero actualmente no aparece como tabla en `docs/database-schema.drawdb.json`.
- Recomendacion: incluirla en el siguiente export de DrawDB para cerrar brecha documental del modulo Auth.
