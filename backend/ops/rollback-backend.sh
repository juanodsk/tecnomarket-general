#!/usr/bin/env bash
set -Eeuo pipefail

required=(PREVIOUS_BACKEND_IMAGE ROLLBACK_HEALTH_URL ROLLBACK_CONFIRM ROLLBACK_COMPATIBILITY_CONFIRMED)
for name in "${required[@]}"; do
  if [[ -z "${!name:-}" ]]; then
    echo "Missing required environment variable: ${name}" >&2
    exit 1
  fi
done

if [[ "${PREVIOUS_BACKEND_IMAGE}" != *@sha256:* ]]; then
  echo "PREVIOUS_BACKEND_IMAGE must be pinned by digest" >&2
  exit 1
fi
if [[ "${ROLLBACK_CONFIRM}" != "ROLLBACK_TECMARKET" ]]; then
  echo "ROLLBACK_CONFIRM must equal ROLLBACK_TECMARKET" >&2
  exit 1
fi
if [[ "${ROLLBACK_COMPATIBILITY_CONFIRMED}" != "true" ]]; then
  echo "Confirm that the previous image is compatible with the current forward-only schema" >&2
  exit 1
fi

compose_file="${COMPOSE_FILE:-docker-compose.yml}"
BACKEND_IMAGE="${PREVIOUS_BACKEND_IMAGE}" docker compose -f "${compose_file}" pull backend
BACKEND_IMAGE="${PREVIOUS_BACKEND_IMAGE}" docker compose -f "${compose_file}" up -d --no-deps backend

for attempt in $(seq 1 20); do
  if curl --fail --silent --show-error "${ROLLBACK_HEALTH_URL}" >/dev/null; then
    echo "Rollback health check passed with ${PREVIOUS_BACKEND_IMAGE}"
    exit 0
  fi
  sleep 3
done

echo "Rollback image did not become ready within 60 seconds" >&2
exit 1
