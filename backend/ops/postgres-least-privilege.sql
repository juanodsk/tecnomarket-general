\set ON_ERROR_STOP on

-- Run as the database owner after creating the two NOLOGIN-external-password roles.
-- Example invocation:
-- psql -v database_name=tecmarket_db -v app_user=tecmarket_app \
--      -v migrator_user=tecmarket_migrator -f postgres-least-privilege.sql

REVOKE CREATE ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON DATABASE :"database_name" FROM PUBLIC;

GRANT CONNECT ON DATABASE :"database_name" TO :"app_user", :"migrator_user";
GRANT USAGE ON SCHEMA public TO :"app_user";
GRANT USAGE, CREATE ON SCHEMA public TO :"migrator_user";
ALTER SCHEMA public OWNER TO :"migrator_user";

-- Transfer existing Flyway-managed objects so the migrator can ALTER them later.
SELECT format('ALTER TABLE %I.%I OWNER TO %I', schemaname, tablename, :'migrator_user')
FROM pg_tables
WHERE schemaname = 'public'
\gexec

SELECT format('ALTER SEQUENCE %I.%I OWNER TO %I', sequence_schema, sequence_name, :'migrator_user')
FROM information_schema.sequences
WHERE sequence_schema = 'public'
\gexec

SELECT format(
    'ALTER FUNCTION %I.%I(%s) OWNER TO %I',
    namespace.nspname,
    procedure.proname,
    pg_get_function_identity_arguments(procedure.oid),
    :'migrator_user')
FROM pg_proc procedure
JOIN pg_namespace namespace ON namespace.oid = procedure.pronamespace
WHERE namespace.nspname = 'public'
\gexec

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO :"app_user";
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO :"app_user";
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO :"app_user";
REVOKE ALL ON TABLE flyway_schema_history FROM :"app_user";

ALTER DEFAULT PRIVILEGES FOR ROLE :"migrator_user" IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO :"app_user";
ALTER DEFAULT PRIVILEGES FOR ROLE :"migrator_user" IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO :"app_user";
ALTER DEFAULT PRIVILEGES FOR ROLE :"migrator_user" IN SCHEMA public
    GRANT EXECUTE ON FUNCTIONS TO :"app_user";

REVOKE CREATE ON SCHEMA public FROM :"app_user";
