import { api } from "@/lib/api/client";
import type { ApiResponse, PageResponse } from "@/types/api";
import type { BusinessCatalogItem } from "@/types/business-service";
import type {
  CreateCustomerDevice,
  CustomerDevice,
  UpdateCustomerDevice,
} from "@/types/customer-device";

const PAGE_SIZE = 100;

async function getCustomerDevicesPage(businessId: number, page: number) {
  const response = await api.get<ApiResponse<PageResponse<CustomerDevice>>>(
    `/api/v1/businesses/${businessId}/customer-devices`,
    { params: { page, size: PAGE_SIZE } },
  );

  return response.data.data;
}

export async function getAllCustomerDevices(businessId: number) {
  const firstPage = await getCustomerDevicesPage(businessId, 0);
  if (firstPage.totalPages <= 1) return firstPage.items;

  const remainingPages = await Promise.all(
    Array.from({ length: firstPage.totalPages - 1 }, (_, index) =>
      getCustomerDevicesPage(businessId, index + 1),
    ),
  );

  return [firstPage, ...remainingPages].flatMap((page) => page.items);
}

export async function getCustomerDeviceCatalog(
  businessId: number,
  catalogType: "DEVICE_TYPE" | "BRAND",
) {
  const response = await api.get<ApiResponse<BusinessCatalogItem[]>>(
    `/api/v1/businesses/${businessId}/catalogs/${catalogType}`,
  );

  return response.data.data;
}

export async function createCustomerDevice(
  businessId: number,
  request: CreateCustomerDevice,
) {
  const response = await api.post<ApiResponse<CustomerDevice>>(
    `/api/v1/businesses/${businessId}/customer-devices`,
    request,
  );

  return response.data.data;
}

export async function updateCustomerDevice(
  businessId: number,
  customerDeviceId: number,
  request: UpdateCustomerDevice,
) {
  const response = await api.put<ApiResponse<CustomerDevice>>(
    `/api/v1/businesses/${businessId}/customer-devices/${customerDeviceId}`,
    request,
  );

  return response.data.data;
}

export async function deleteCustomerDevice(businessId: number, customerDeviceId: number) {
  await api.delete<ApiResponse<null>>(
    `/api/v1/businesses/${businessId}/customer-devices/${customerDeviceId}`,
  );
}
