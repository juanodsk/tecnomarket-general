#!/usr/bin/env bash
set -Eeuo pipefail

ZAP_IMAGE="ghcr.io/zaproxy/zaproxy:stable@sha256:781a2bdaea47324e7bab583e2263f21d257b0aee61ed51521a5be45f5f5081ef"
target="${QA_TARGET_URL:?Set QA_TARGET_URL to the staging base URL}"
report_dir="${QA_REPORT_DIR:-$(pwd)/target/qa/zap}"

if [[ "$target" != https://* && "${QA_ALLOW_HTTP:-false}" != true ]]; then
  echo "DAST requires HTTPS. Set QA_ALLOW_HTTP=true only for an isolated local target." >&2
  exit 1
fi

mkdir -p "$report_dir"
report_dir="$(cd "$report_dir" && pwd)"

docker run --rm \
  -v "${report_dir}:/zap/wrk:rw" \
  "$ZAP_IMAGE" zap-baseline.py \
  -t "$target" \
  -m "${QA_ZAP_SPIDER_MINUTES:-2}" \
  -I \
  -J zap-report.json \
  -w zap-report.md \
  -r zap-report.html

if grep -Eq '"riskcode"[[:space:]]*:[[:space:]]*"(3|4)"' "${report_dir}/zap-report.json"; then
  echo "ZAP found at least one high or critical alert." >&2
  exit 1
fi

echo "ZAP baseline reports: ${report_dir}"
