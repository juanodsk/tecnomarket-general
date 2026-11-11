#!/usr/bin/env bash
set -Eeuo pipefail

umask 077

required=(MINIO_TARGET_ALIAS RESTORE_FILE BACKUP_ENCRYPTION_PASSWORD_FILE RESTORE_CONFIRM_ALIAS)
for name in "${required[@]}"; do
  if [[ -z "${!name:-}" ]]; then
    echo "Missing required environment variable: ${name}" >&2
    exit 1
  fi
done

if [[ "${RESTORE_CONFIRM_ALIAS}" != "${MINIO_TARGET_ALIAS}" ]]; then
  echo "RESTORE_CONFIRM_ALIAS must exactly match MINIO_TARGET_ALIAS" >&2
  exit 1
fi

for command_name in mc tar openssl sha256sum; do
  command -v "${command_name}" >/dev/null || {
    echo "Required command not found: ${command_name}" >&2
    exit 1
  }
done

if [[ ! -f "${RESTORE_FILE}" || ! -f "${RESTORE_FILE}.sha256" ]]; then
  echo "Encrypted backup and matching .sha256 file are required" >&2
  exit 1
fi
if [[ ! -f "${BACKUP_ENCRYPTION_PASSWORD_FILE}" ]]; then
  echo "BACKUP_ENCRYPTION_PASSWORD_FILE must be an existing file" >&2
  exit 1
fi

(cd "$(dirname "${RESTORE_FILE}")" && sha256sum --check "$(basename "${RESTORE_FILE}").sha256")
work_dir="$(mktemp -d)"
trap 'rm -rf -- "${work_dir}"' EXIT
archive="${work_dir}/restore.tar"

openssl enc -d -aes-256-cbc -pbkdf2 -iter 250000 \
  -pass "file:${BACKUP_ENCRYPTION_PASSWORD_FILE}" \
  -in "${RESTORE_FILE}" \
  -out "${archive}"
tar -C "${work_dir}" -xf "${archive}"

for source in "${work_dir}"/objects/*; do
  bucket="$(basename "${source}")"
  mc mb --ignore-existing "${MINIO_TARGET_ALIAS}/${bucket}"
  mc mirror --preserve "${source}" "${MINIO_TARGET_ALIAS}/${bucket}"
done

echo "MinIO restore completed without deleting objects already present at the target"
