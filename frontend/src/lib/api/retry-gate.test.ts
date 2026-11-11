import { afterEach, describe, expect, it, vi } from "vitest";

import { assertRequestAllowed, blockRequest, clearRetryBlocks } from "@/lib/api/retry-gate";

afterEach(() => {
  clearRetryBlocks();
  vi.useRealTimers();
});

describe("retry gate", () => {
  it("bloquea únicamente la ruta limitada durante Retry-After", () => {
    vi.useFakeTimers();
    vi.setSystemTime(new Date("2026-09-13T12:00:00Z"));
    blockRequest("/api/v1/auth/login", 5);

    expect(() => assertRequestAllowed("/api/v1/auth/login")).toThrow("Espera 5 segundos");
    expect(() => assertRequestAllowed("/api/v1/auth/me")).not.toThrow();

    vi.advanceTimersByTime(5000);
    expect(() => assertRequestAllowed("/api/v1/auth/login")).not.toThrow();
  });
});
