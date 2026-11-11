export type BusinessCatalogType =
  | "SERVICE_CATEGORY"
  | "DEVICE_TYPE"
  | "BRAND"
  | "APPOINTMENT_STATUS"
  | "REPAIR_ORDER_STATUS"
  | "QUOTATION_STATUS"
  | "ITEM_CATEGORY"
  | "MOVEMENT_TYPE"
  | "PAYMENT_METHOD";

export interface BusinessCatalogItem {
  id: number;
  businessId: number;
  catalogType: BusinessCatalogType;
  code: string;
  name: string;
  description: string | null;
  icon: string | null;
  sortOrder: number | null;
  protectedItem: boolean;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface BusinessService {
  id: number;
  businessId: number;
  serviceCategoryId: number;
  serviceCategoryCode: string;
  serviceCategoryName: string;
  deviceTypeId: number;
  deviceTypeCode: string;
  deviceTypeName: string;
  name: string;
  description: string;
  basePrice: number;
  estimatedTimeMinutes: number;
  imageUrl: string | null;
  active: boolean;
}

export interface CreateBusinessService {
  serviceCategoryId: number;
  deviceTypeId: number;
  name: string;
  description: string;
  basePrice: number;
  estimatedTimeMinutes: number;
}

export interface UpdateBusinessService extends CreateBusinessService {
  active: boolean;
}
