import type { CustomerDevice } from "@/types/customer-device";
import type { RepairOrder } from "@/types/repair-order";

export interface CreateQuickRepairOrder {
  customer: {
    documentTypeCode: string;
    documentNumber: string;
    name: string;
    surname: string;
    email: string;
    address: string;
    phone: string;
    countryId: number;
    departmentId: number;
    cityId: number;
  };
  device: {
    deviceTypeId: number;
    brandId: number;
    model: string;
    serialNumber: string | null;
    imei: string | null;
    color: string;
    registeredAt: string | null;
  };
  order: {
    branchId: number;
    businessServiceId: number;
    assignedBusinessUserId: number;
    problemDescription: string;
    diagnosis: string | null;
    estimatedPrice: number | null;
    estimatedDeliveryAt: string | null;
    receivedAt: string | null;
  };
}

export interface QuickRepairOrderResponse {
  customer: {
    id: number;
    documentTypeCode: string;
    documentNumber: string;
    name: string;
    surname: string;
    email: string;
    phone: string;
    emailVerified: boolean;
  };
  device: CustomerDevice;
  repairOrder: RepairOrder;
}
