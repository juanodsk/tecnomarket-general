# TecMarket production operations

Estos scripts son guardas operativas para el piloto. No contienen credenciales y no
se ejecutan automáticamente desde el repositorio.

## Requisitos comunes

- Ejecutarlos desde un host de operaciones aislado, no desde el contenedor backend.
- Guardar `PGPASSFILE` y `BACKUP_ENCRYPTION_PASSWORD_FILE` con permisos `0600`.
- Montar `BACKUP_OUTPUT_DIR` sobre almacenamiento externo al host de producción.
- Conservar el archivo cifrado y su `.sha256` juntos.
- Usar TLS para PostgreSQL y los alias de MinIO. Los scripts usan
  `PGSSLMODE=require` por defecto.

## PostgreSQL

`postgres-least-privilege.sql` separa el usuario runtime del migrador Flyway. El
usuario de aplicación conserva DML, secuencias y funciones, pero no puede crear
objetos. Ejecutarlo como propietario de la base y comprobar los grants antes del
primer despliegue.

Programar `postgres-backup.sh` al menos una vez al día. El archivo se produce en
formato custom de `pg_dump`, se cifra con AES-256/PBKDF2 y recibe un checksum SHA-256.

Para ensayar una restauración, crear una base vacía de staging, detener el backend
de staging y ejecutar `postgres-restore.sh`. La variable
`RESTORE_CONFIRM_DATABASE` debe coincidir exactamente con `PGDATABASE`; el script
rechaza la operación en caso contrario. Al finalizar comprueba Flyway y `app_user`.

## MinIO

Configurar un alias `mc` de solo lectura para el origen. `minio-backup.sh` copia los
tres buckets requeridos a un directorio temporal, cifra el archivo y genera su
checksum. Restaurar siempre primero sobre un alias de staging con
`minio-restore.sh`; el script no elimina objetos adicionales del destino.

## Rollback

`rollback-backend.sh` solo acepta una imagen anterior fijada mediante digest y exige
dos confirmaciones explícitas. Antes de ejecutarlo se debe comprobar que esa versión
de la aplicación es compatible con el esquema Flyway actual, porque las migraciones
son forward-only. Después del éxito, actualizar el digest guardado en Dokploy para
que el siguiente reconcile no revierta accidentalmente el rollback.

## Alertas

`prometheus/tecmarket-alerts.yml` contiene el conjunto mínimo para disponibilidad,
5xx, p95, fallos de login/rate limit, saturación de Hikari, memoria JVM y expiración
TLS. Ajustar el label `job` al scrape real de Dokploy y dirigir `critical` a una ruta
de atención inmediata antes de habilitar el piloto.
