import { api } from "@/lib/api/client";
import type { ApiResponse, PageResponse } from "@/types/api";
import type { PlatformUser, PlatformUserRole } from "@/types/platform-user";

export async function getPlatformUsers(page = 0, size = 20) {
  const response = await api.get<ApiResponse<PageResponse<PlatformUser>>>(
    "/api/v1/parameters/users",
    { params: { page, size } },
  );

  return response.data.data;
}

export async function getPlatformUser(userId: number) {
  const response = await api.get<ApiResponse<PlatformUser>>(`/api/v1/parameters/users/${userId}`);

  return response.data.data;
}

export async function updatePlatformUserStatus(userId: number, active: boolean) {
  const response = await api.patch<ApiResponse<PlatformUser>>(
    `/api/v1/parameters/users/${userId}/status`,
    { active },
  );

  return response.data.data;
}

export async function revokePlatformUserSessions(userId: number) {
  await api.delete<ApiResponse<null>>(`/api/v1/parameters/users/${userId}/sessions`);
}

export async function getPlatformUserRoles(userId: number) {
  const response = await api.get<ApiResponse<PlatformUserRole[]>>(
    `/api/v1/parameters/users/${userId}/roles`,
  );

  return response.data.data;
}

export async function updatePlatformUserRoles(userId: number, roleIds: number[]) {
  const response = await api.put<ApiResponse<PlatformUserRole[]>>(
    `/api/v1/parameters/users/${userId}/roles`,
    { roleIds },
  );

  return response.data.data;
}
