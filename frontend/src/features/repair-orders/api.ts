import { api } from "@/lib/api/client";
import type { ApiResponse, PageResponse } from "@/types/api";
import type {
  BusinessTechnicianOption,
  CreateRepairOrder,
  RepairOrderAttachment,
  RepairOrder,
  RepairOrderFilters,
  RepairOrderHistoryEntry,
  UpdateRepairOrderStatusRequest,
} from "@/types/repair-order";
import type { CreateQuotationRequest, Quotation, UpdateQuotationStatusRequest } from "@/types/quotation";
import type { CreateQuickRepairOrder, QuickRepairOrderResponse } from "@/types/quick-repair-order";

export async function getRepairOrders(
  businessId: number,
  page = 0,
  size = 20,
  filters: RepairOrderFilters = {},
) {
  const response = await api.get<ApiResponse<PageResponse<RepairOrder>>>(
    `/api/v1/businesses/${businessId}/repair-orders`,
    {
      params: {
        ...filters,
        page,
        size,
        sortBy: "receivedAt",
        sortDir: "desc",
      },
    },
  );

  return response.data.data;
}

export async function getRepairOrderHistory(
  businessId: number,
  repairOrderId: number,
  page = 0,
  size = 20,
) {
  const response = await api.get<ApiResponse<PageResponse<RepairOrderHistoryEntry>>>(
    `/api/v1/businesses/${businessId}/repair-orders/${repairOrderId}/history`,
    { params: { page, size } },
  );

  return response.data.data;
}

export async function updateRepairOrderStatus(
  businessId: number,
  repairOrderId: number,
  request: UpdateRepairOrderStatusRequest,
) {
  const response = await api.patch<ApiResponse<RepairOrder>>(
    `/api/v1/businesses/${businessId}/repair-orders/${repairOrderId}/status`,
    request,
  );

  return response.data.data;
}

export async function getRepairOrderQuotations(
  businessId: number,
  repairOrderId: number,
  page = 0,
  size = 20,
) {
  const response = await api.get<ApiResponse<PageResponse<Quotation>>>(
    `/api/v1/businesses/${businessId}/repair-orders/${repairOrderId}/quotations`,
    { params: { page, size } },
  );

  return response.data.data;
}

export async function createRepairOrderQuotation(
  businessId: number,
  request: CreateQuotationRequest,
) {
  const response = await api.post<ApiResponse<Quotation>>(
    `/api/v1/businesses/${businessId}/quotations`,
    request,
  );

  return response.data.data;
}

export async function updateRepairOrderQuotationStatus(
  businessId: number,
  quotationId: number,
  request: UpdateQuotationStatusRequest,
) {
  const response = await api.patch<ApiResponse<Quotation>>(
    `/api/v1/businesses/${businessId}/quotations/${quotationId}/status`,
    request,
  );

  return response.data.data;
}

export async function updateRepairOrderDiagnosis(
  businessId: number,
  repairOrderId: number,
  diagnosis: string,
) {
  const response = await api.patch<ApiResponse<RepairOrder>>(
    `/api/v1/businesses/${businessId}/repair-orders/${repairOrderId}/diagnosis`,
    { diagnosis },
  );

  return response.data.data;
}

export async function getRepairOrderAttachments(
  businessId: number,
  repairOrderId: number,
  page = 0,
  size = 20,
) {
  const response = await api.get<ApiResponse<PageResponse<RepairOrderAttachment>>>(
    `/api/v1/businesses/${businessId}/repair-orders/${repairOrderId}/attachments`,
    { params: { page, size } },
  );

  return response.data.data;
}

export async function uploadRepairOrderAttachment(
  businessId: number,
  repairOrderId: number,
  file: File,
  description?: string,
) {
  const formData = new FormData();
  formData.append("file", file);
  if (description?.trim()) formData.append("description", description.trim());

  const response = await api.post<ApiResponse<RepairOrderAttachment>>(
    `/api/v1/businesses/${businessId}/repair-orders/${repairOrderId}/attachments/upload`,
    formData,
  );

  return response.data.data;
}

export async function deleteRepairOrderAttachment(
  businessId: number,
  repairOrderId: number,
  attachmentId: number,
) {
  await api.delete<ApiResponse<null>>(
    `/api/v1/businesses/${businessId}/repair-orders/${repairOrderId}/attachments/${attachmentId}`,
  );
}

export async function createRepairOrder(
  businessId: number,
  request: CreateRepairOrder,
) {
  const response = await api.post<ApiResponse<RepairOrder>>(
    `/api/v1/businesses/${businessId}/repair-orders`,
    request,
  );

  return response.data.data;
}

export async function createQuickRepairOrder(
  businessId: number,
  request: CreateQuickRepairOrder,
  idempotencyKey: string,
) {
  const response = await api.post<ApiResponse<QuickRepairOrderResponse>>(
    `/api/v1/businesses/${businessId}/repair-orders/quick`,
    request,
    { headers: { "Idempotency-Key": idempotencyKey } },
  );

  return response.data.data;
}

export async function getRepairOrderTechnicians(businessId: number, branchId?: number) {
  const response = await api.get<ApiResponse<BusinessTechnicianOption[]>>(
    `/api/v1/businesses/${businessId}/users/technicians`,
    { params: branchId ? { branchId } : undefined },
  );

  return response.data.data;
}
