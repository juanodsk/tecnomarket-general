export interface PlatformUser {
  id: number;
  documentTypeCode: string;
  documentTypeName: string;
  documentNumber: string;
  name: string;
  surname: string;
  email: string;
  phone: string;
  address: string;
  countryId: number;
  countryName: string;
  departmentId: number;
  departmentName: string;
  cityId: number;
  cityName: string;
  active: boolean;
  emailVerified: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface PlatformUserRole {
  roleId: number;
  code: string;
  name: string;
  description: string;
  scope: string;
  assigned: boolean;
}
