import { api } from "@/lib/api/client";
import type { ApiResponse, PageResponse } from "@/types/api";
import type {
  Business,
  BusinessBranch,
  BusinessContext,
  BusinessRoleOption,
  BusinessRequest,
  BusinessUser,
  BusinessUserCandidate,
  CreateBusinessBranch,
  CreateBusinessRequest,
  CreateBusinessUser,
  RejectBusinessRequest,
  UpdateBusinessBranch,
  UpdateBusinessUser,
} from "@/types/business";

export async function getMyBusinesses() {
  const response = await api.get<ApiResponse<Business[]>>("/api/v1/businesses/my");

  return response.data.data;
}

export async function getBusinessContext(businessId: number) {
  const response = await api.get<ApiResponse<BusinessContext>>(
    `/api/v1/businesses/${businessId}/me/context`,
  );

  return response.data.data;
}

export async function getBusinessBranches(businessId: number) {
  const response = await api.get<ApiResponse<BusinessBranch[]>>(
    `/api/v1/businesses/${businessId}/branches`,
  );

  return response.data.data;
}

export async function createBusinessBranch(businessId: number, request: CreateBusinessBranch) {
  const response = await api.post<ApiResponse<BusinessBranch>>(
    `/api/v1/businesses/${businessId}/branches`,
    request,
  );

  return response.data.data;
}

export async function updateBusinessBranch(
  businessId: number,
  branchId: number,
  request: UpdateBusinessBranch,
) {
  const response = await api.put<ApiResponse<BusinessBranch>>(
    `/api/v1/businesses/${businessId}/branches/${branchId}`,
    request,
  );

  return response.data.data;
}

export async function deleteBusinessBranch(businessId: number, branchId: number) {
  await api.delete<ApiResponse<null>>(`/api/v1/businesses/${businessId}/branches/${branchId}`);
}

export async function getBusinessUsers(businessId: number, page = 0, size = 20) {
  const response = await api.get<ApiResponse<PageResponse<BusinessUser>>>(
    `/api/v1/businesses/${businessId}/users`,
    { params: { page, size } },
  );

  return response.data.data;
}

export async function getBusinessRoleOptions(businessId: number) {
  const response = await api.get<ApiResponse<BusinessRoleOption[]>>(
    `/api/v1/businesses/${businessId}/users/role-options`,
  );

  return response.data.data;
}

export async function findAvailableBusinessUsers(businessId: number, email: string) {
  const response = await api.get<ApiResponse<BusinessUserCandidate[]>>(
    `/api/v1/businesses/${businessId}/users/available-users`,
    { params: { email } },
  );

  return response.data.data;
}

export async function createBusinessUser(businessId: number, request: CreateBusinessUser) {
  const response = await api.post<ApiResponse<BusinessUser>>(
    `/api/v1/businesses/${businessId}/users`,
    request,
  );

  return response.data.data;
}

export async function updateBusinessUser(
  businessId: number,
  businessUserId: number,
  request: UpdateBusinessUser,
) {
  const response = await api.put<ApiResponse<BusinessUser>>(
    `/api/v1/businesses/${businessId}/users/${businessUserId}`,
    request,
  );

  return response.data.data;
}

export async function deleteBusinessUser(businessId: number, businessUserId: number) {
  await api.delete<ApiResponse<null>>(
    `/api/v1/businesses/${businessId}/users/${businessUserId}`,
  );
}

export async function getBusinesses(page = 0, size = 20) {
  const response = await api.get<ApiResponse<PageResponse<Business>>>("/api/v1/businesses", {
    params: { page, size },
  });

  return response.data.data;
}

export async function getBusinessById(businessId: number) {
  const response = await api.get<ApiResponse<Business>>(`/api/v1/businesses/${businessId}`);

  return response.data.data;
}

export async function getPendingBusinessRequests(page = 0, size = 20) {
  const response = await api.get<ApiResponse<PageResponse<BusinessRequest>>>(
    "/api/v1/business/requests/pending",
    { params: { page, size } },
  );

  return response.data.data;
}

export async function getMyBusinessRequests(page = 0, size = 100) {
  const response = await api.get<ApiResponse<PageResponse<BusinessRequest>>>(
    "/api/v1/business/requests/my",
    { params: { page, size } },
  );

  return response.data.data;
}

export async function createBusinessRequest(request: CreateBusinessRequest) {
  const response = await api.post<ApiResponse<BusinessRequest>>(
    "/api/v1/business/requests",
    request,
  );

  return response.data.data;
}

export async function approveBusinessRequest(requestId: number) {
  const response = await api.put<ApiResponse<BusinessRequest>>(
    `/api/v1/business/requests/${requestId}/approve`,
  );

  return response.data.data;
}

export async function rejectBusinessRequest(requestId: number, request: RejectBusinessRequest) {
  const response = await api.put<ApiResponse<BusinessRequest>>(
    `/api/v1/business/requests/${requestId}/reject`,
    request,
  );

  return response.data.data;
}
