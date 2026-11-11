import { AxiosError, AxiosHeaders, type InternalAxiosRequestConfig } from "axios";
import { describe, expect, it } from "vitest";

import { normalizeApiError } from "@/lib/api/errors";

describe("normalizeApiError", () => {
  it("conserva status, Retry-After y correlation id", () => {
    const config = { headers: new AxiosHeaders() } as InternalAxiosRequestConfig;
    const error = new AxiosError("rate limited", "ERR_BAD_REQUEST", config, undefined, {
      status: 429,
      statusText: "Too Many Requests",
      headers: new AxiosHeaders({ "retry-after": "18", "x-correlation-id": "corr-17" }),
      config,
      data: { success: false, message: "Espera un momento", data: null },
    });

    expect(normalizeApiError(error)).toMatchObject({
      status: 429,
      retryAfter: 18,
      correlationId: "corr-17",
      message: "Espera un momento",
    });
  });

  it("usa un mensaje de conexión cuando no hay respuesta", () => {
    expect(normalizeApiError(new AxiosError("network"))).toMatchObject({ status: 0 });
  });
});
