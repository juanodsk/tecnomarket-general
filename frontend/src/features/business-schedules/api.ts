import { api } from "@/lib/api/client";
import type { ApiResponse, PageResponse } from "@/types/api";
import type {
  BusinessSchedule,
  CreateBusinessSchedule,
  UpdateBusinessSchedule,
} from "@/types/business-schedule";

const PAGE_SIZE = 100;

async function getBusinessSchedulesPage(businessId: number, page: number) {
  const response = await api.get<ApiResponse<PageResponse<BusinessSchedule>>>(
    `/api/v1/businesses/${businessId}/schedules`,
    { params: { page, size: PAGE_SIZE } },
  );

  return response.data.data;
}

export async function getAllBusinessSchedules(businessId: number) {
  const firstPage = await getBusinessSchedulesPage(businessId, 0);
  if (firstPage.totalPages <= 1) return firstPage.items;

  const remainingPages = await Promise.all(
    Array.from({ length: firstPage.totalPages - 1 }, (_, index) =>
      getBusinessSchedulesPage(businessId, index + 1),
    ),
  );

  return [firstPage, ...remainingPages].flatMap((page) => page.items);
}

export async function createBusinessSchedule(
  businessId: number,
  request: CreateBusinessSchedule,
) {
  const response = await api.post<ApiResponse<BusinessSchedule>>(
    `/api/v1/businesses/${businessId}/schedules`,
    request,
  );

  return response.data.data;
}

export async function updateBusinessSchedule(
  businessId: number,
  scheduleId: number,
  request: UpdateBusinessSchedule,
) {
  const response = await api.put<ApiResponse<BusinessSchedule>>(
    `/api/v1/businesses/${businessId}/schedules/${scheduleId}`,
    request,
  );

  return response.data.data;
}

export async function deleteBusinessSchedule(businessId: number, scheduleId: number) {
  await api.delete<ApiResponse<null>>(
    `/api/v1/businesses/${businessId}/schedules/${scheduleId}`,
  );
}
