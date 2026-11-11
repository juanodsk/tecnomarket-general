export type AccessScope = "GLOBAL" | "BUSINESS";

export interface PlatformRole {
  id: number;
  code: string;
  name: string;
  description: string;
  scope: AccessScope;
  active: boolean;
}

export interface RolePermissionOption {
  permissionId: number;
  code: string;
  name: string;
  description: string;
  scope: AccessScope;
  moduleId: number;
  moduleCode: string;
  moduleName: string;
  assigned: boolean;
}

export interface PlatformPermission {
  id: number;
  moduleId: number;
  moduleCode: string;
  moduleName: string;
  code: string;
  name: string;
  description: string;
  active: boolean;
}

export interface CreateRoleRequest {
  code: string;
  name: string;
  description: string;
  scope: AccessScope;
}

export interface UpdateRoleRequest {
  name: string;
  description: string;
  scope: AccessScope;
  active?: boolean;
}
