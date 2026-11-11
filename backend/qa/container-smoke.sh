#!/usr/bin/env bash
set -Eeuo pipefail

BACKEND_IMAGE="${1:-${QA_BACKEND_IMAGE:-tecmarket-backend:qa}}"
POSTGRES_IMAGE="postgres:17-alpine@sha256:18cfe3ef5e6815560c98237d6216d1e5119702fb0f3894c8785dd58b8bbe5d73"
REDIS_IMAGE="redis:8-alpine@sha256:becdda6c7f4b3fb42e42fd7f120bbf5c54c4caaaf16f26da24e4563d2c1f0576"
MINIO_IMAGE="quay.io/minio/minio:RELEASE.2025-04-22T22-12-26Z@sha256:a1ea29fa28355559ef137d71fc570e508a214ec84ff8083e39bc5428980b015e"
MINIO_CLIENT_IMAGE="quay.io/minio/mc:latest@sha256:a7fe349ef4bd8521fb8497f55c6042871b2ae640607cf99d9bede5e9bdf11727"

require_command() {
  command -v "$1" >/dev/null 2>&1 || { echo "Required command is missing: $1" >&2; exit 1; }
}

require_command docker
require_command curl
require_command openssl

pull_image() {
  local image="$1"
  local attempt
  for attempt in 1 2 3; do
    if docker pull "$image" >/dev/null; then
      return 0
    fi
    echo "Image pull attempt ${attempt}/3 failed for ${image}." >&2
    sleep $((attempt * 2))
  done
  echo "Unable to pull required image after three attempts: ${image}" >&2
  return 1
}

suffix="$(openssl rand -hex 4)"
network="tecmarket-qa-${suffix}"
postgres="tecmarket-qa-postgres-${suffix}"
redis="tecmarket-qa-redis-${suffix}"
minio="tecmarket-qa-minio-${suffix}"
backend="tecmarket-qa-backend-${suffix}"
host_port="${QA_BACKEND_PORT:-}"
database="tecmarket_qa"
migrator="qa_migrator"
runtime="qa_runtime"
db_password="$(openssl rand -hex 24)"
runtime_password="$(openssl rand -hex 24)"
redis_password="$(openssl rand -hex 24)"
minio_access="qa$(openssl rand -hex 8)"
minio_secret="$(openssl rand -hex 24)"
jwt_secret="$(openssl rand -hex 48)"
resend_key="re_$(openssl rand -hex 16)"
response_dir="$(mktemp -d)"

assert_no_sensitive_values() {
  local file="$1"
  ! grep -Eqi 'stack(trace)?|exception|org\.springframework|jdbc:' "$file"
  ! grep -Fq "$db_password" "$file"
  ! grep -Fq "$runtime_password" "$file"
  ! grep -Fq "$redis_password" "$file"
  ! grep -Fq "$minio_secret" "$file"
  ! grep -Fq "$jwt_secret" "$file"
}

cleanup() {
  docker rm -f "$backend" "$minio" "$redis" "$postgres" >/dev/null 2>&1 || true
  docker network rm "$network" >/dev/null 2>&1 || true
  rm -rf "$response_dir"
}
trap cleanup EXIT
trap 'status=$?; echo "Container smoke failed at line ${LINENO}." >&2; exit "$status"' ERR

docker image inspect "$BACKEND_IMAGE" >/dev/null
pull_image "$POSTGRES_IMAGE"
pull_image "$REDIS_IMAGE"
pull_image "$MINIO_IMAGE"
pull_image "$MINIO_CLIENT_IMAGE"
docker network create "$network" >/dev/null

docker run -d --name "$postgres" --network "$network" \
  -e POSTGRES_DB="$database" \
  -e POSTGRES_USER="$migrator" \
  -e POSTGRES_PASSWORD="$db_password" \
  "$POSTGRES_IMAGE" >/dev/null

for _ in {1..60}; do
  if docker logs "$postgres" 2>&1 \
      | grep -F 'PostgreSQL init process complete; ready for start up.' >/dev/null; then
    break
  fi
  sleep 1
done
docker logs "$postgres" 2>&1 \
  | grep -F 'PostgreSQL init process complete; ready for start up.' >/dev/null

for _ in {1..40}; do
  if docker exec "$postgres" pg_isready -U "$migrator" -d "$database" >/dev/null 2>&1; then
    break
  fi
  sleep 1
done
docker exec "$postgres" pg_isready -U "$migrator" -d "$database" >/dev/null
docker exec "$postgres" psql -v ON_ERROR_STOP=1 -U "$migrator" -d "$database" \
  -c "CREATE ROLE ${runtime} LOGIN PASSWORD '${runtime_password}'; GRANT CONNECT ON DATABASE ${database} TO ${runtime}; GRANT USAGE ON SCHEMA public TO ${runtime}; ALTER DEFAULT PRIVILEGES FOR ROLE ${migrator} IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO ${runtime}; ALTER DEFAULT PRIVILEGES FOR ROLE ${migrator} IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO ${runtime};" >/dev/null

docker run -d --name "$redis" --network "$network" \
  "$REDIS_IMAGE" redis-server --save "" --appendonly no --requirepass "$redis_password" >/dev/null

docker run -d --name "$minio" --network "$network" \
  -e MINIO_ROOT_USER="$minio_access" \
  -e MINIO_ROOT_PASSWORD="$minio_secret" \
  "$MINIO_IMAGE" server /data >/dev/null

for _ in {1..40}; do
  if docker run --rm --network "$network" \
      -e "MC_HOST_qa=http://${minio_access}:${minio_secret}@${minio}:9000" \
      "$MINIO_CLIENT_IMAGE" ready qa >/dev/null 2>&1; then
    break
  fi
  sleep 1
done
docker run --rm --network "$network" \
  -e "MC_HOST_qa=http://${minio_access}:${minio_secret}@${minio}:9000" \
  "$MINIO_CLIENT_IMAGE" mb --ignore-existing \
  qa/business-assets qa/service-images qa/repair-order-evidence >/dev/null

port_binding="127.0.0.1::8080"
if [[ -n "$host_port" ]]; then
  port_binding="127.0.0.1:${host_port}:8080"
fi

docker run -d --name "$backend" --network "$network" --read-only \
  --tmpfs /tmp:rw,noexec,nosuid,size=128m \
  --cap-drop ALL \
  -p "$port_binding" \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://${postgres}:5432/${database}" \
  -e SPRING_DATASOURCE_USERNAME="$runtime" \
  -e SPRING_DATASOURCE_PASSWORD="$runtime_password" \
  -e SPRING_FLYWAY_USERNAME="$migrator" \
  -e SPRING_FLYWAY_PASSWORD="$db_password" \
  -e SPRING_DATA_REDIS_HOST="$redis" \
  -e SPRING_DATA_REDIS_PASSWORD="$redis_password" \
  -e JWT_SECRET="$jwt_secret" \
  -e CORS_ALLOWED_ORIGINS=https://qa.tecmarket.example \
  -e TRUSTED_PROXY_RANGES=127.0.0.1/32 \
  -e STORAGE_MINIO_ENDPOINT="http://${minio}:9000" \
  -e STORAGE_MINIO_ACCESS_KEY="$minio_access" \
  -e STORAGE_MINIO_SECRET_KEY="$minio_secret" \
  -e STORAGE_MINIO_PUBLIC_BASE_URL=https://assets-qa.tecmarket.example \
  -e STORAGE_MINIO_PRIVATE_DOWNLOAD_BASE_URL=https://evidence-qa.tecmarket.example \
  -e RESEND_API_KEY="$resend_key" \
  -e EMAIL_VERIFICATION_VERIFY_URL=https://qa.tecmarket.example/verify-email \
  -e EMAIL_VERIFICATION_FROM=no-reply@tecmarket.example \
  -e PASSWORD_RESET_URL=https://qa.tecmarket.example/restablecer-contrasena \
  "$BACKEND_IMAGE" >/dev/null

if [[ -z "$host_port" ]]; then
  host_port="$(docker port "$backend" 8080/tcp | awk -F: 'END {print $NF}')"
fi
[[ "$host_port" =~ ^[0-9]+$ ]]

base_url="http://127.0.0.1:${host_port}"
ready=false
for _ in {1..120}; do
  if curl --fail --silent --max-time 2 "${base_url}/actuator/health/readiness" \
      -o "${response_dir}/readiness.json"; then
    ready=true
    break
  fi
  if ! docker inspect "$backend" --format '{{.State.Running}}' 2>/dev/null | grep -q true; then
    docker logs "$backend" >&2
    exit 1
  fi
  sleep 1
done

if [[ "$ready" != true ]]; then
  docker logs "$backend" >&2
  echo "Backend readiness timed out" >&2
  exit 1
fi

grep -Eq '^\{"status":"UP"\}$' "${response_dir}/readiness.json"

curl --silent --show-error --dump-header "${response_dir}/headers.txt" \
  -o "${response_dir}/unauthorized.json" -w '%{http_code}' \
  "${base_url}/api/v1/parameters/roles" | grep -qx '401'
grep -qi '^X-Content-Type-Options: nosniff' "${response_dir}/headers.txt"
grep -qi '^X-Frame-Options: DENY' "${response_dir}/headers.txt"
grep -qi '^Referrer-Policy: strict-origin-when-cross-origin' "${response_dir}/headers.txt"
grep -qi '^Permissions-Policy:' "${response_dir}/headers.txt"
grep -qi '^Content-Security-Policy:' "${response_dir}/headers.txt"
grep -qi '^Content-Type: application/json' "${response_dir}/headers.txt"
assert_no_sensitive_values "${response_dir}/unauthorized.json"

curl --silent --show-error -H 'Content-Type: application/json' \
  -d '{"email":"invalid","password":"x"}' \
  -o "${response_dir}/invalid-login.json" -w '%{http_code}' \
  "${base_url}/api/v1/auth/login" | grep -qx '400'
assert_no_sensitive_values "${response_dir}/invalid-login.json"

for disabled_path in /v3/api-docs /swagger-ui.html; do
  curl --silent --show-error -o /dev/null -w '%{http_code}' \
    "${base_url}${disabled_path}" | grep -qx '404'
done

curl --silent --show-error -o /dev/null -w '%{http_code}' \
  "${base_url}/actuator/prometheus" | grep -qx '401'

echo "Container smoke passed: production profile, PostgreSQL/Flyway, Redis, MinIO, security headers and fail-closed endpoints."
