import type { City, Country, Department, DocumentType } from "@/types/location";

export type { City, Country, Department, DocumentType };

export interface AppModule {
  id: number;
  code: string;
  name: string;
  active: boolean;
}

export interface CreateCountryRequest {
  code: string;
  name: string;
}

export interface UpdateCountryRequest {
  name: string;
  active?: boolean;
}

export interface CreateDepartmentRequest {
  countryId: number;
  code: string;
  name: string;
}

export interface UpdateDepartmentRequest extends CreateDepartmentRequest {
  active?: boolean;
}

export interface CreateCityRequest {
  departmentId: number;
  name: string;
}

export interface UpdateCityRequest extends CreateCityRequest {
  active?: boolean;
}

export interface CreateDocumentTypeRequest {
  code: string;
  name: string;
  description: string;
  appliesToPerson: boolean;
  appliesToCompany: boolean;
}

export interface UpdateDocumentTypeRequest extends Omit<CreateDocumentTypeRequest, "code"> {
  active?: boolean;
}

export interface CreateAppModuleRequest {
  code: string;
  name: string;
}

export interface UpdateAppModuleRequest {
  name: string;
  active?: boolean;
}

