import { api } from "@/lib/api/client";
import type { PlatformPermission } from "@/types/access-control";
import type { ApiResponse } from "@/types/api";
import type {
  AppModule,
  City,
  Country,
  CreateAppModuleRequest,
  CreateCityRequest,
  CreateCountryRequest,
  CreateDepartmentRequest,
  CreateDocumentTypeRequest,
  Department,
  DocumentType,
  UpdateAppModuleRequest,
  UpdateCityRequest,
  UpdateCountryRequest,
  UpdateDepartmentRequest,
  UpdateDocumentTypeRequest,
} from "@/types/platform-parameters";

async function read<T>(path: string) {
  const response = await api.get<ApiResponse<T>>(path);
  return response.data.data;
}

async function create<T, TRequest>(path: string, request: TRequest) {
  const response = await api.post<ApiResponse<T>>(path, request);
  return response.data.data;
}

async function update<T, TRequest>(path: string, request: TRequest) {
  const response = await api.put<ApiResponse<T>>(path, request);
  return response.data.data;
}

async function remove(path: string) {
  await api.delete<ApiResponse<null>>(path);
}

export const getCountries = () => read<Country[]>("/api/v1/parameters/countries");
export const getCountry = (id: number) => read<Country>(`/api/v1/parameters/countries/${id}`);
export const createCountry = (request: CreateCountryRequest) => create<Country, CreateCountryRequest>("/api/v1/parameters/countries", request);
export const updateCountry = (id: number, request: UpdateCountryRequest) => update<Country, UpdateCountryRequest>(`/api/v1/parameters/countries/${id}`, request);
export const deleteCountry = (id: number) => remove(`/api/v1/parameters/countries/${id}`);

export const getDepartments = () => read<Department[]>("/api/v1/parameters/departments");
export const getDepartment = (id: number) => read<Department>(`/api/v1/parameters/departments/${id}`);
export const createDepartment = (request: CreateDepartmentRequest) => create<Department, CreateDepartmentRequest>("/api/v1/parameters/departments", request);
export const updateDepartment = (id: number, request: UpdateDepartmentRequest) => update<Department, UpdateDepartmentRequest>(`/api/v1/parameters/departments/${id}`, request);
export const deleteDepartment = (id: number) => remove(`/api/v1/parameters/departments/${id}`);

export const getCities = () => read<City[]>("/api/v1/parameters/cities");
export const getCity = (id: number) => read<City>(`/api/v1/parameters/cities/${id}`);
export const createCity = (request: CreateCityRequest) => create<City, CreateCityRequest>("/api/v1/parameters/cities", request);
export const updateCity = (id: number, request: UpdateCityRequest) => update<City, UpdateCityRequest>(`/api/v1/parameters/cities/${id}`, request);
export const deleteCity = (id: number) => remove(`/api/v1/parameters/cities/${id}`);

export const getDocumentTypes = () => read<DocumentType[]>("/api/v1/parameters/document-types");
export const getDocumentType = (id: number) => read<DocumentType>(`/api/v1/parameters/document-types/${id}`);
export const createDocumentType = (request: CreateDocumentTypeRequest) => create<DocumentType, CreateDocumentTypeRequest>("/api/v1/parameters/document-types", request);
export const updateDocumentType = (id: number, request: UpdateDocumentTypeRequest) => update<DocumentType, UpdateDocumentTypeRequest>(`/api/v1/parameters/document-types/${id}`, request);
export const deleteDocumentType = (id: number) => remove(`/api/v1/parameters/document-types/${id}`);

export const getModules = () => read<AppModule[]>("/api/v1/parameters/modules");
export const getModule = (id: number) => read<AppModule>(`/api/v1/parameters/modules/${id}`);
export const createModule = (request: CreateAppModuleRequest) => create<AppModule, CreateAppModuleRequest>("/api/v1/parameters/modules", request);
export const updateModule = (id: number, request: UpdateAppModuleRequest) => update<AppModule, UpdateAppModuleRequest>(`/api/v1/parameters/modules/${id}`, request);
export const deleteModule = (id: number) => remove(`/api/v1/parameters/modules/${id}`);

export const getPermissions = () => read<PlatformPermission[]>("/api/v1/parameters/permissions");

