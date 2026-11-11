import axios, { type InternalAxiosRequestConfig } from "axios";

import { notifyUnauthorized } from "@/lib/auth/auth-events";
import { normalizeApiError } from "@/lib/api/errors";
import { assertRequestAllowed, blockRequest, clearRetryBlocks } from "@/lib/api/retry-gate";
import { clearAccessToken, getAccessToken, setAccessToken } from "@/lib/api/token-store";
import type { ApiResponse } from "@/types/api";
import type { LoginResponse } from "@/types/auth";

declare module "axios" {
  export interface AxiosRequestConfig {
    skipAuthRefresh?: boolean;
    retriedAfterRefresh?: boolean;
  }
}

export const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_URL?.replace(/\/$/, "") ?? "https://api-dev.juanodsk.dev";

const defaults = {
  baseURL: API_BASE_URL,
  withCredentials: true,
  headers: { "Accept-Language": "es" },
};

export const rawApi = axios.create(defaults);
export const api = axios.create(defaults);

let refreshPromise: Promise<string> | null = null;

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  assertRequestAllowed(config.url);
  const token = getAccessToken();

  if (token) config.headers.Authorization = `Bearer ${token}`;

  return config;
});

async function requestFreshAccessToken() {
  const response = await rawApi.post<ApiResponse<LoginResponse>>("/api/v1/auth/refresh");
  const token = response.data.data.accessToken;

  setAccessToken(token);

  return token;
}

export function refreshAccessToken() {
  if (!refreshPromise) {
    refreshPromise = requestFreshAccessToken().finally(() => {
      refreshPromise = null;
    });
  }

  return refreshPromise;
}

api.interceptors.response.use(
  (response) => response,
  async (error: unknown) => {
    if (axios.isAxiosError(error)) {
      const config = error.config;
      const isUnauthorized = error.response?.status === 401;

      if (config && isUnauthorized && !config.skipAuthRefresh && !config.retriedAfterRefresh) {
        config.retriedAfterRefresh = true;

        try {
          const token = await refreshAccessToken();

          config.headers = config.headers ?? {};
          config.headers.Authorization = `Bearer ${token}`;

          return api.request(config);
        } catch (refreshError) {
          clearAccessToken();
          notifyUnauthorized();
          throw normalizeApiError(refreshError);
        }
      }
    }

    const normalized = normalizeApiError(error);

    if (normalized.status === 429 && axios.isAxiosError(error)) {
      blockRequest(error.config?.url, normalized.retryAfter);
    }

    throw normalized;
  },
);

export function resetApiSession() {
  clearAccessToken();
  refreshPromise = null;
  clearRetryBlocks();
}
