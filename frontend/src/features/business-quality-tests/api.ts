import { api } from "@/lib/api/client";
import type { ApiResponse, PageResponse } from "@/types/api";
import type { BusinessCatalogItem } from "@/types/business-service";
import type {
  BusinessQualityTest,
  CreateBusinessQualityTest,
  UpdateBusinessQualityTest,
} from "@/types/business-quality-test";

const PAGE_SIZE = 100;

async function getQualityTestsPage(businessId: number, page: number) {
  const response = await api.get<ApiResponse<PageResponse<BusinessQualityTest>>>(
    `/api/v1/businesses/${businessId}/quality-tests`,
    { params: { page, size: PAGE_SIZE } },
  );

  return response.data.data;
}

export async function getAllBusinessQualityTests(businessId: number) {
  const firstPage = await getQualityTestsPage(businessId, 0);
  if (firstPage.totalPages <= 1) return firstPage.items;

  const remainingPages = await Promise.all(
    Array.from({ length: firstPage.totalPages - 1 }, (_, index) =>
      getQualityTestsPage(businessId, index + 1),
    ),
  );

  return [firstPage, ...remainingPages].flatMap((page) => page.items);
}

export async function getDeviceTypes(businessId: number) {
  const response = await api.get<ApiResponse<BusinessCatalogItem[]>>(
    `/api/v1/businesses/${businessId}/catalogs/DEVICE_TYPE`,
  );

  return response.data.data;
}

export async function createBusinessQualityTest(
  businessId: number,
  request: CreateBusinessQualityTest,
) {
  const response = await api.post<ApiResponse<BusinessQualityTest>>(
    `/api/v1/businesses/${businessId}/quality-tests`,
    request,
  );

  return response.data.data;
}

export async function updateBusinessQualityTest(
  businessId: number,
  qualityTestId: number,
  request: UpdateBusinessQualityTest,
) {
  const response = await api.put<ApiResponse<BusinessQualityTest>>(
    `/api/v1/businesses/${businessId}/quality-tests/${qualityTestId}`,
    request,
  );

  return response.data.data;
}

export async function deleteBusinessQualityTest(businessId: number, qualityTestId: number) {
  await api.delete<ApiResponse<null>>(
    `/api/v1/businesses/${businessId}/quality-tests/${qualityTestId}`,
  );
}
