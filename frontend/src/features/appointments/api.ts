import { api } from "@/lib/api/client";
import type { ApiResponse, PageResponse } from "@/types/api";
import type {
  Appointment,
  CreateAppointment,
  UpdateAppointment,
} from "@/types/appointment";

export async function getAppointments(businessId: number, page = 0, size = 20) {
  const response = await api.get<ApiResponse<PageResponse<Appointment>>>(
    `/api/v1/businesses/${businessId}/appointments`,
    { params: { page, size } },
  );

  return response.data.data;
}

export async function createAppointment(
  businessId: number,
  request: CreateAppointment,
) {
  const response = await api.post<ApiResponse<Appointment>>(
    `/api/v1/businesses/${businessId}/appointments`,
    request,
  );

  return response.data.data;
}

export async function updateAppointment(
  businessId: number,
  appointmentId: number,
  request: UpdateAppointment,
) {
  const response = await api.put<ApiResponse<Appointment>>(
    `/api/v1/businesses/${businessId}/appointments/${appointmentId}`,
    request,
  );

  return response.data.data;
}

export async function updateAppointmentStatus(
  businessId: number,
  appointmentId: number,
  statusId: number,
) {
  const response = await api.patch<ApiResponse<Appointment>>(
    `/api/v1/businesses/${businessId}/appointments/${appointmentId}/status`,
    { statusId },
  );

  return response.data.data;
}

export async function deleteAppointment(businessId: number, appointmentId: number) {
  await api.delete<ApiResponse<null>>(
    `/api/v1/businesses/${businessId}/appointments/${appointmentId}`,
  );
}
