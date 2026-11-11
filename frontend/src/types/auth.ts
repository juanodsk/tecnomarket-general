export interface AuthUser {
  id: number;
  documentTypeCode: string;
  documentNumber: string;
  name: string;
  surname: string;
  email: string;
  phone: string;
  countryId: number;
  countryName: string;
  departmentId: number;
  departmentName: string;
  cityId: number;
  cityName: string;
}

export interface AuthPermissions {
  userId: number;
  roles: string[];
  permissions: string[];
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  user: AuthUser;
}

export interface RegisterRequest {
  documentTypeCode: string;
  documentNumber: string;
  name: string;
  surname: string;
  email: string;
  password: string;
  address: string;
  phone: string;
  countryId: number;
  departmentId: number;
  cityId: number;
}

export interface AuthSession {
  sessionId: string;
  clientDescription: string | null;
  ipAddress: string | null;
  createdAt: string;
  lastUsedAt: string;
  expiresAt: string;
}
