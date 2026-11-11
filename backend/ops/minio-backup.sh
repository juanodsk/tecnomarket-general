#!/usr/bin/env bash
set -Eeuo pipefail

umask 077

required=(MINIO_SOURCE_ALIAS BACKUP_OUTPUT_DIR BACKUP_ENCRYPTION_PASSWORD_FILE)
for name in "${required[@]}"; do
  if [[ -z "${!name:-}" ]]; then
    echo "Missing required environment variable: ${name}" >&2
    exit 1
  fi
done

for command_name in mc tar openssl sha256sum; do
  command -v "${command_name}" >/dev/null || {
    echo "Required command not found: ${command_name}" >&2
    exit 1
  }
done

mkdir -p -- "${BACKUP_OUTPUT_DIR}"
if [[ ! -f "${BACKUP_ENCRYPTION_PASSWORD_FILE}" ]]; then
  echo "BACKUP_ENCRYPTION_PASSWORD_FILE must be an existing file" >&2
  exit 1
fi
work_dir="$(mktemp -d)"
trap 'rm -rf -- "${work_dir}"' EXIT

buckets=(
  "${STORAGE_MINIO_BUCKET_BUSINESS_ASSETS:-business-assets}"
  "${STORAGE_MINIO_BUCKET_SERVICE_IMAGES:-service-images}"
  "${STORAGE_MINIO_BUCKET_REPAIR_EVIDENCE:-repair-order-evidence}"
)

for bucket in "${buckets[@]}"; do
  mkdir -p -- "${work_dir}/objects/${bucket}"
  mc mirror --preserve "${MINIO_SOURCE_ALIAS}/${bucket}" "${work_dir}/objects/${bucket}"
done

timestamp="$(date -u +%Y%m%dT%H%M%SZ)"
archive="${work_dir}/tecmarket-minio-${timestamp}.tar"
encrypted_archive="${BACKUP_OUTPUT_DIR}/tecmarket-minio-${timestamp}.tar.enc"
tar -C "${work_dir}" -cf "${archive}" objects

openssl enc -aes-256-cbc -salt -pbkdf2 -iter 250000 \
  -pass "file:${BACKUP_ENCRYPTION_PASSWORD_FILE}" \
  -in "${archive}" \
  -out "${encrypted_archive}"

(cd "${BACKUP_OUTPUT_DIR}" && sha256sum "$(basename "${encrypted_archive}")" > "$(basename "${encrypted_archive}").sha256")
echo "Encrypted MinIO backup created: ${encrypted_archive}"
