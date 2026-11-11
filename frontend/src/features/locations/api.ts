import { api } from "@/lib/api/client";
import type { ApiResponse } from "@/types/api";
import type { City, Country, Department, DocumentType } from "@/types/location";

export async function getDocumentTypes() {
  const response = await api.get<ApiResponse<DocumentType[]>>(
    "/api/v1/parameters/document-types",
    { skipAuthRefresh: true },
  );

  return response.data.data;
}

export async function getCountries() {
  const response = await api.get<ApiResponse<Country[]>>(
    "/api/v1/public/locations/countries",
    { skipAuthRefresh: true },
  );

  return response.data.data;
}

export async function getDepartments(countryId: number) {
  const response = await api.get<ApiResponse<Department[]>>(
    `/api/v1/public/locations/countries/${countryId}/departments`,
    { skipAuthRefresh: true },
  );

  return response.data.data;
}

export async function getCities(departmentId: number) {
  const response = await api.get<ApiResponse<City[]>>(
    `/api/v1/public/locations/departments/${departmentId}/cities`,
    { skipAuthRefresh: true },
  );

  return response.data.data;
}
