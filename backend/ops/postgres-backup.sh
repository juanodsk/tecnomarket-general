#!/usr/bin/env bash
set -Eeuo pipefail

umask 077

required=(PGHOST PGDATABASE PGUSER PGPASSFILE BACKUP_OUTPUT_DIR BACKUP_ENCRYPTION_PASSWORD_FILE)
for name in "${required[@]}"; do
  if [[ -z "${!name:-}" ]]; then
    echo "Missing required environment variable: ${name}" >&2
    exit 1
  fi
done

for command_name in pg_dump openssl sha256sum; do
  command -v "${command_name}" >/dev/null || {
    echo "Required command not found: ${command_name}" >&2
    exit 1
  }
done

if [[ ! -f "${PGPASSFILE}" || ! -f "${BACKUP_ENCRYPTION_PASSWORD_FILE}" ]]; then
  echo "PGPASSFILE and BACKUP_ENCRYPTION_PASSWORD_FILE must be existing files" >&2
  exit 1
fi

mkdir -p -- "${BACKUP_OUTPUT_DIR}"
work_dir="$(mktemp -d)"
trap 'rm -rf -- "${work_dir}"' EXIT

timestamp="$(date -u +%Y%m%dT%H%M%SZ)"
base_name="tecmarket-postgres-${timestamp}"
plain_dump="${work_dir}/${base_name}.dump"
encrypted_dump="${BACKUP_OUTPUT_DIR}/${base_name}.dump.enc"

export PGPORT="${PGPORT:-5432}"
export PGSSLMODE="${PGSSLMODE:-require}"

pg_dump \
  --format=custom \
  --compress=9 \
  --no-owner \
  --no-acl \
  --file="${plain_dump}" \
  "${PGDATABASE}"

openssl enc -aes-256-cbc -salt -pbkdf2 -iter 250000 \
  -pass "file:${BACKUP_ENCRYPTION_PASSWORD_FILE}" \
  -in "${plain_dump}" \
  -out "${encrypted_dump}"

(cd "${BACKUP_OUTPUT_DIR}" && sha256sum "$(basename "${encrypted_dump}")" > "$(basename "${encrypted_dump}").sha256")
echo "Encrypted PostgreSQL backup created: ${encrypted_dump}"
