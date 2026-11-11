export interface DocumentType {
  id: number;
  code: string;
  name: string;
  description: string;
  appliesToPerson: boolean;
  appliesToCompany: boolean;
  active: boolean;
}

export interface Country {
  id: number;
  code: string;
  name: string;
  active: boolean;
}

export interface Department {
  id: number;
  countryId: number;
  countryCode: string;
  countryName: string;
  code: string;
  name: string;
  active: boolean;
}

export interface City {
  id: number;
  countryId: number;
  countryCode: string;
  countryName: string;
  departmentId: number;
  departmentCode: string;
  departmentName: string;
  name: string;
  active: boolean;
}
