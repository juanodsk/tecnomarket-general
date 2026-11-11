import { ApiClientError } from "@/lib/api/errors";

const blockedUntil = new Map<string, number>();

function keyFor(url?: string) {
  return url?.split("?")[0] ?? "unknown";
}

export function blockRequest(url: string | undefined, retryAfter: number | null) {
  if (!retryAfter || retryAfter <= 0) return;
  blockedUntil.set(keyFor(url), Date.now() + retryAfter * 1000);
}

export function assertRequestAllowed(url?: string) {
  const key = keyFor(url);
  const until = blockedUntil.get(key) ?? 0;
  const remaining = Math.ceil((until - Date.now()) / 1000);

  if (remaining <= 0) {
    blockedUntil.delete(key);
    return;
  }

  throw new ApiClientError({
    status: 429,
    retryAfter: remaining,
    message: `Espera ${remaining} segundos antes de intentarlo nuevamente.`,
  });
}

export function clearRetryBlocks() {
  blockedUntil.clear();
}
