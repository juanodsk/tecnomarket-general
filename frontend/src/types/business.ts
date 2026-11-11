export interface Business {
  id: number;
  documentTypeCode: string;
  documentNumber: string;
  name: string;
  legalName: string;
  email: string;
  phone: string;
  address: string;
  description: string | null;
  cityId: number;
  cityName: string;
  logo: string | null;
  coverImage: string | null;
  ratingAverage: number;
  ratingCount: number;
  active: boolean;
}

export interface BusinessContextBranch {
  id: number;
  branchNumber: number;
  name: string;
  cityId: number;
  cityName: string;
  address: string;
  primaryBranch: boolean;
}

export interface BusinessContext {
  userId: number;
  businessId: number;
  businessName: string;
  businessLogo: string | null;
  platformAdmin: boolean;
  businessUserId: number | null;
  roleId: number | null;
  roleCode: string | null;
  roleName: string | null;
  permissions: string[];
  branches: BusinessContextBranch[];
}

export interface BusinessBranch {
  id: number;
  businessId: number;
  businessName: string;
  branchNumber: number;
  name: string;
  countryId: number;
  countryName: string;
  cityId: number;
  cityName: string;
  address: string;
  neighborhood: string | null;
  phone: string | null;
  latitude: number | null;
  longitude: number | null;
  serviceRadiusKm: number | null;
  primaryBranch: boolean;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateBusinessBranch {
  name: string;
  cityId: number;
  address: string;
  neighborhood: string | null;
  phone: string | null;
  latitude: number | null;
  longitude: number | null;
  serviceRadiusKm: number | null;
  primaryBranch: boolean;
}

export interface UpdateBusinessBranch extends CreateBusinessBranch {
  active: boolean;
}

export interface BusinessUser {
  id: number;
  businessId: number;
  userId: number;
  userName: string;
  userSurname: string;
  userEmail: string;
  roleId: number;
  roleCode: string;
  roleName: string;
  branchIds: number[];
  commissionPercentage: number | null;
  active: boolean;
}

export interface BusinessRoleOption {
  roleId: number;
  roleCode: string;
  roleName: string;
}

export interface BusinessUserCandidate {
  userId: number;
  fullName: string;
  email: string;
  assigned: boolean;
}

export interface CreateBusinessUser {
  userId: number;
  roleId: number;
  branchIds: number[];
  commissionPercentage: number | null;
}

export interface UpdateBusinessUser {
  roleId: number;
  branchIds: number[];
  active: boolean;
  commissionPercentage: number | null;
}

export type BusinessRequestStatus = "PENDING" | "APPROVED" | "REJECTED";

export interface BusinessRequest {
  id: number;
  documentTypeCode: string;
  documentNumber: string;
  businessName: string;
  legalName: string | null;
  email: string;
  phone: string;
  cityId: number;
  cityName: string;
  address: string | null;
  legalFile: string;
  status: BusinessRequestStatus;
  requestedByUserId: number;
  requestedByUserName: string;
  reviewedByUserId: number | null;
  reviewedByUserName: string | null;
  reviewedAt: string | null;
  rejectionReason: string | null;
  createdAt: string;
}

export interface RejectBusinessRequest {
  rejectionReason: string;
}

export interface CreateBusinessRequest {
  documentTypeCode: string;
  documentNumber: string;
  businessName: string;
  legalName: string;
  email: string;
  phone: string;
  cityId: number;
  address: string;
  legalFile: string;
}
