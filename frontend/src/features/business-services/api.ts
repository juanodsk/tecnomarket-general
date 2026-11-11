import { api } from "@/lib/api/client";
import type { ApiResponse, PageResponse } from "@/types/api";
import type {
  BusinessCatalogItem,
  BusinessCatalogType,
  BusinessService,
  CreateBusinessService,
  UpdateBusinessService,
} from "@/types/business-service";

export async function getBusinessCatalogItems(
  businessId: number,
  catalogType: BusinessCatalogType,
) {
  const response = await api.get<ApiResponse<BusinessCatalogItem[]>>(
    `/api/v1/businesses/${businessId}/catalogs/${catalogType}`,
  );

  return response.data.data;
}

export async function getBusinessServices(businessId: number, page = 0, size = 12) {
  const response = await api.get<ApiResponse<PageResponse<BusinessService>>>(
    `/api/v1/businesses/${businessId}/services`,
    { params: { page, size } },
  );

  return response.data.data;
}

export async function getAllBusinessServices(businessId: number) {
  const firstPage = await getBusinessServices(businessId, 0, 100);
  if (firstPage.totalPages <= 1) return firstPage.items;

  const remainingPages = await Promise.all(
    Array.from({ length: firstPage.totalPages - 1 }, (_, index) =>
      getBusinessServices(businessId, index + 1, 100),
    ),
  );

  return [firstPage, ...remainingPages].flatMap((page) => page.items);
}

export async function createBusinessService(
  businessId: number,
  request: CreateBusinessService,
) {
  const response = await api.post<ApiResponse<BusinessService>>(
    `/api/v1/businesses/${businessId}/services`,
    request,
  );

  return response.data.data;
}

export async function updateBusinessService(
  businessId: number,
  serviceId: number,
  request: UpdateBusinessService,
) {
  const response = await api.put<ApiResponse<BusinessService>>(
    `/api/v1/businesses/${businessId}/services/${serviceId}`,
    request,
  );

  return response.data.data;
}

export async function uploadBusinessServiceImage(
  businessId: number,
  serviceId: number,
  file: File,
) {
  const formData = new FormData();
  formData.append("file", file);
  const response = await api.post<ApiResponse<BusinessService>>(
    `/api/v1/businesses/${businessId}/services/${serviceId}/image`,
    formData,
  );

  return response.data.data;
}

export async function deleteBusinessService(businessId: number, serviceId: number) {
  await api.delete<ApiResponse<null>>(
    `/api/v1/businesses/${businessId}/services/${serviceId}`,
  );
}
