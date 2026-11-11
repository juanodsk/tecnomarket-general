#!/usr/bin/env bash
set -Eeuo pipefail

umask 077

required=(PGHOST PGDATABASE PGUSER PGPASSFILE RESTORE_FILE BACKUP_ENCRYPTION_PASSWORD_FILE RESTORE_CONFIRM_DATABASE)
for name in "${required[@]}"; do
  if [[ -z "${!name:-}" ]]; then
    echo "Missing required environment variable: ${name}" >&2
    exit 1
  fi
done

if [[ "${RESTORE_CONFIRM_DATABASE}" != "${PGDATABASE}" ]]; then
  echo "RESTORE_CONFIRM_DATABASE must exactly match PGDATABASE" >&2
  exit 1
fi

for command_name in pg_restore psql openssl sha256sum; do
  command -v "${command_name}" >/dev/null || {
    echo "Required command not found: ${command_name}" >&2
    exit 1
  }
done

if [[ ! -f "${RESTORE_FILE}" || ! -f "${RESTORE_FILE}.sha256" ]]; then
  echo "Encrypted backup and matching .sha256 file are required" >&2
  exit 1
fi
if [[ ! -f "${PGPASSFILE}" || ! -f "${BACKUP_ENCRYPTION_PASSWORD_FILE}" ]]; then
  echo "PGPASSFILE and BACKUP_ENCRYPTION_PASSWORD_FILE must be existing files" >&2
  exit 1
fi

(cd "$(dirname "${RESTORE_FILE}")" && sha256sum --check "$(basename "${RESTORE_FILE}").sha256")
work_dir="$(mktemp -d)"
trap 'rm -rf -- "${work_dir}"' EXIT
plain_dump="${work_dir}/restore.dump"

openssl enc -d -aes-256-cbc -pbkdf2 -iter 250000 \
  -pass "file:${BACKUP_ENCRYPTION_PASSWORD_FILE}" \
  -in "${RESTORE_FILE}" \
  -out "${plain_dump}"

export PGPORT="${PGPORT:-5432}"
export PGSSLMODE="${PGSSLMODE:-require}"

pg_restore \
  --exit-on-error \
  --clean \
  --if-exists \
  --no-owner \
  --no-acl \
  --dbname="${PGDATABASE}" \
  "${plain_dump}"

psql --dbname="${PGDATABASE}" --set=ON_ERROR_STOP=1 --tuples-only --command \
  "SELECT version, success FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 1;"
psql --dbname="${PGDATABASE}" --set=ON_ERROR_STOP=1 --tuples-only --command \
  "SELECT COUNT(*) AS app_user_count FROM app_user;"

echo "PostgreSQL restore completed and basic schema checks passed"
