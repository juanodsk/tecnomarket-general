export interface CustomerDevice {
  id: number;
  businessId: number;
  customerId: number;
  customerDocumentTypeCode: string;
  customerDocumentNumber: string;
  customerName: string;
  customerSurname: string;
  customerEmail: string;
  deviceTypeId: number;
  deviceTypeCode: string;
  deviceTypeName: string;
  brandId: number;
  brandName: string;
  model: string;
  serialNumber: string | null;
  imei: string | null;
  registeredByBusinessUserId: number;
  registeredByBusinessUserName: string;
  color: string;
  registeredAt: string;
  active: boolean;
}

export interface CreateCustomerDevice {
  customerId: number;
  deviceTypeId: number;
  brandId: number;
  model: string;
  serialNumber: string | null;
  imei: string | null;
  registeredByBusinessUserId: number;
  color: string;
  registeredAt: string | null;
}

export interface UpdateCustomerDevice extends CreateCustomerDevice {
  active: boolean;
}

export interface CustomerDirectoryEntry {
  id: number;
  documentTypeCode: string;
  documentNumber: string;
  name: string;
  surname: string;
  email: string;
  devices: CustomerDevice[];
}
