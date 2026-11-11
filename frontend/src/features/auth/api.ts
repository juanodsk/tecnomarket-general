import { api, refreshAccessToken } from "@/lib/api/client";
import { setAccessToken } from "@/lib/api/token-store";
import type { ApiResponse } from "@/types/api";
import type {
  AuthPermissions,
  AuthSession,
  AuthUser,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
} from "@/types/auth";

export async function registerAccount(request: RegisterRequest) {
  const response = await api.post<ApiResponse<AuthUser>>("/api/v1/auth/register", request, {
    skipAuthRefresh: true,
  });

  return response.data;
}

export async function login(request: LoginRequest) {
  const response = await api.post<ApiResponse<LoginResponse>>("/api/v1/auth/login", request, {
    skipAuthRefresh: true,
  });

  setAccessToken(response.data.data.accessToken);

  return response.data.data;
}

export async function verifyEmail(token: string) {
  const response = await api.post<ApiResponse<null>>(
    "/api/v1/auth/verify-email",
    { token },
    { skipAuthRefresh: true },
  );

  return response.data;
}

export async function requestPasswordReset(email: string) {
  const response = await api.post<ApiResponse<null>>(
    "/api/v1/auth/password-reset/request",
    { email },
    { skipAuthRefresh: true },
  );

  return response.data;
}

export async function resetPassword(token: string, newPassword: string) {
  const response = await api.post<ApiResponse<null>>(
    "/api/v1/auth/password-reset/confirm",
    { token, newPassword },
    { skipAuthRefresh: true },
  );

  return response.data;
}

export async function changePassword(currentPassword: string, newPassword: string) {
  const response = await api.post<ApiResponse<null>>(
    "/api/v1/auth/password/change",
    { currentPassword, newPassword },
    { skipAuthRefresh: true },
  );

  return response.data;
}

export async function getMe() {
  const response = await api.get<ApiResponse<AuthUser>>("/api/v1/auth/me");

  return response.data.data;
}

export async function getMyPermissions() {
  const response = await api.get<ApiResponse<AuthPermissions>>("/api/v1/auth/me/permissions");

  return response.data.data;
}

export async function logout() {
  const response = await api.post<ApiResponse<null>>("/api/v1/auth/logout", undefined, {
    skipAuthRefresh: true,
  });

  return response.data;
}

export async function getSessions() {
  const response = await api.get<ApiResponse<AuthSession[]>>("/api/v1/auth/sessions");

  return response.data.data;
}

export async function revokeSession(sessionId: string) {
  const response = await api.delete<ApiResponse<null>>(`/api/v1/auth/sessions/${sessionId}`);

  return response.data;
}

export async function revokeAllSessions() {
  const response = await api.delete<ApiResponse<null>>("/api/v1/auth/sessions", {
    skipAuthRefresh: true,
  });

  return response.data;
}

export async function restoreAccessToken() {
  return refreshAccessToken();
}
