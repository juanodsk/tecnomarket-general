import { api } from "@/lib/api/client";
import type { ApiResponse } from "@/types/api";
import type {
  CreateRoleRequest,
  PlatformPermission,
  PlatformRole,
  RolePermissionOption,
  UpdateRoleRequest,
} from "@/types/access-control";

export async function getRoles() {
  const response = await api.get<ApiResponse<PlatformRole[]>>("/api/v1/parameters/roles");
  return response.data.data;
}

export async function getRole(roleId: number) {
  const response = await api.get<ApiResponse<PlatformRole>>(`/api/v1/parameters/roles/${roleId}`);
  return response.data.data;
}

export async function createRole(request: CreateRoleRequest) {
  const response = await api.post<ApiResponse<PlatformRole>>("/api/v1/parameters/roles", request);
  return response.data.data;
}

export async function updateRole(roleId: number, request: UpdateRoleRequest) {
  const response = await api.put<ApiResponse<PlatformRole>>(`/api/v1/parameters/roles/${roleId}`, request);
  return response.data.data;
}

export async function deleteRole(roleId: number) {
  await api.delete<ApiResponse<null>>(`/api/v1/parameters/roles/${roleId}`);
}

export async function getRolePermissions(roleId: number) {
  const response = await api.get<ApiResponse<RolePermissionOption[]>>(
    `/api/v1/parameters/roles/${roleId}/permissions`,
  );
  return response.data.data;
}

export async function updateRolePermissions(roleId: number, permissionIds: number[]) {
  const response = await api.put<ApiResponse<RolePermissionOption[]>>(
    `/api/v1/parameters/roles/${roleId}/permissions`,
    { permissionIds },
  );
  return response.data.data;
}

export async function getPermissions() {
  const response = await api.get<ApiResponse<PlatformPermission[]>>("/api/v1/parameters/permissions");
  return response.data.data;
}
