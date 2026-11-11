import axios from "axios";

import type { ApiResponse } from "@/types/api";

const FALLBACK_MESSAGES: Record<number, string> = {
  400: "Revisa los datos enviados e intenta de nuevo.",
  401: "Tu sesión ya no es válida. Inicia sesión nuevamente.",
  403: "No tienes permiso para realizar esta acción.",
  404: "No encontramos el recurso solicitado.",
  409: "La operación entra en conflicto con el estado actual.",
  413: "El archivo supera el tamaño permitido.",
  415: "El tipo de archivo o contenido no está permitido.",
  429: "Has realizado demasiados intentos. Espera antes de continuar.",
  503: "El servicio no está disponible temporalmente.",
};

export class ApiClientError extends Error {
  readonly status: number;
  readonly retryAfter: number | null;
  readonly correlationId: string | null;
  readonly data: unknown;

  constructor(options: {
    message: string;
    status?: number;
    retryAfter?: number | null;
    correlationId?: string | null;
    data?: unknown;
  }) {
    super(options.message);
    this.name = "ApiClientError";
    this.status = options.status ?? 0;
    this.retryAfter = options.retryAfter ?? null;
    this.correlationId = options.correlationId ?? null;
    this.data = options.data;
  }
}

export function normalizeApiError(error: unknown): ApiClientError {
  if (error instanceof ApiClientError) return error;

  if (!axios.isAxiosError(error)) {
    return new ApiClientError({ message: "Ocurrió un error inesperado. Intenta nuevamente." });
  }

  const status = error.response?.status ?? 0;
  const payload = error.response?.data as Partial<ApiResponse<unknown>> | undefined;
  const retryHeader = error.response?.headers?.["retry-after"];
  const correlationHeader =
    error.response?.headers?.["x-correlation-id"] ?? error.response?.headers?.["x-correlationid"];
  const rawRetryAfter = retryHeader == null ? null : String(retryHeader);
  const seconds = rawRetryAfter == null ? Number.NaN : Number.parseInt(rawRetryAfter, 10);
  const dateSeconds = rawRetryAfter == null
    ? Number.NaN
    : Math.max(0, Math.ceil((new Date(rawRetryAfter).getTime() - Date.now()) / 1000));
  const retryAfter = Number.isFinite(seconds) ? seconds : dateSeconds;

  return new ApiClientError({
    status,
    message:
      (typeof payload?.message === "string" && payload.message.trim()) ||
      FALLBACK_MESSAGES[status] ||
      (status === 0
        ? "No fue posible conectar con TecMarket. Revisa tu conexión."
        : "No fue posible completar la operación."),
    retryAfter: Number.isFinite(retryAfter) ? retryAfter : null,
    correlationId: correlationHeader ? String(correlationHeader) : null,
    data: payload?.data,
  });
}

export function apiErrorMessage(error: unknown) {
  return normalizeApiError(error).message;
}
