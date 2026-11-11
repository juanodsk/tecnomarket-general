import { AxiosError, AxiosHeaders, type AxiosResponse, type InternalAxiosRequestConfig } from "axios";
import { afterEach, describe, expect, it, vi } from "vitest";

import { api, rawApi, refreshAccessToken, resetApiSession } from "@/lib/api/client";
import { getAccessToken, setAccessToken } from "@/lib/api/token-store";

const tokenResponse = {
  data: { success: true, message: "ok", data: { accessToken: "fresh-token", user: {} } },
} as AxiosResponse;

afterEach(() => {
  vi.restoreAllMocks();
  resetApiSession();
  delete api.defaults.adapter;
});

describe("refresh de sesión", () => {
  it("comparte una sola promesa entre solicitudes concurrentes", async () => {
    const post = vi.spyOn(rawApi, "post").mockResolvedValue(tokenResponse);

    const [first, second, third] = await Promise.all([
      refreshAccessToken(),
      refreshAccessToken(),
      refreshAccessToken(),
    ]);

    expect([first, second, third]).toEqual(["fresh-token", "fresh-token", "fresh-token"]);
    expect(post).toHaveBeenCalledTimes(1);
    expect(getAccessToken()).toBe("fresh-token");
  });

  it("reintenta una petición 401 solo una vez", async () => {
    vi.spyOn(rawApi, "post").mockResolvedValue(tokenResponse);
    let attempts = 0;
    api.defaults.adapter = async (config: InternalAxiosRequestConfig) => {
      attempts += 1;
      if (attempts === 1) {
        throw new AxiosError("unauthorized", "ERR_BAD_REQUEST", config, undefined, {
          status: 401,
          statusText: "Unauthorized",
          headers: new AxiosHeaders(),
          config,
          data: { success: false, message: "No autorizado", data: null },
        });
      }
      return { status: 200, statusText: "OK", headers: {}, config, data: { success: true } };
    };

    await expect(api.get("/protected")).resolves.toMatchObject({ status: 200 });
    expect(attempts).toBe(2);
  });

  it("no intenta un segundo refresh si el reintento vuelve a responder 401", async () => {
    const post = vi.spyOn(rawApi, "post").mockResolvedValue(tokenResponse);
    let attempts = 0;
    api.defaults.adapter = async (config: InternalAxiosRequestConfig) => {
      attempts += 1;
      throw new AxiosError("unauthorized", "ERR_BAD_REQUEST", config, undefined, {
        status: 401,
        statusText: "Unauthorized",
        headers: new AxiosHeaders(),
        config,
        data: { success: false, message: "No autorizado", data: null },
      });
    };

    await expect(api.get("/protected")).rejects.toMatchObject({ status: 401 });
    expect(attempts).toBe(2);
    expect(post).toHaveBeenCalledTimes(1);
  });

  it("limpia el access token cuando falla el refresh", async () => {
    setAccessToken("expired-token");
    vi.spyOn(rawApi, "post").mockRejectedValue(new AxiosError("refresh failed"));
    api.defaults.adapter = async (config: InternalAxiosRequestConfig) => {
      throw new AxiosError("unauthorized", "ERR_BAD_REQUEST", config, undefined, {
        status: 401,
        statusText: "Unauthorized",
        headers: new AxiosHeaders(),
        config,
        data: { success: false, message: "No autorizado", data: null },
      });
    };

    await expect(api.get("/protected")).rejects.toBeDefined();
    expect(getAccessToken()).toBeNull();
  });
});
