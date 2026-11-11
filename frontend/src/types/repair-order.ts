export interface RepairOrder {
  id: number;
  orderNumber: string;
  orderSequence: number;
  businessId: number;
  branchId: number;
  branchNumber: number;
  branchName: string;
  customerId: number;
  customerName: string;
  customerSurname: string;
  customerEmail: string;
  customerDeviceId: number;
  customerDeviceLabel: string;
  appointmentId: number | null;
  businessServiceId: number;
  businessServiceName: string;
  statusId: number;
  statusCode: string;
  statusName: string;
  receivedByBusinessUserId: number;
  receivedByBusinessUserName: string;
  assignedBusinessUserId: number;
  assignedBusinessUserName: string;
  problemDescription: string;
  diagnosis: string | null;
  estimatedPrice: number | null;
  finalPrice: number | null;
  receivedAt: string;
  estimatedDeliveryAt: string | null;
  deliveredAt: string | null;
  active: boolean;
}

export interface CreateRepairOrder {
  branchId: number;
  customerId: number;
  customerDeviceId: number;
  appointmentId: number | null;
  businessServiceId: number;
  assignedBusinessUserId: number;
  problemDescription: string;
  diagnosis: string | null;
  estimatedPrice: number | null;
  estimatedDeliveryAt: string | null;
  receivedAt: string | null;
}

export interface RepairOrderFilters {
  branchId?: number;
  orderNumber?: string;
  statusId?: number;
  customer?: string;
  receivedFrom?: string;
  receivedTo?: string;
}

export interface RepairOrderHistoryEntry {
  id: number;
  repairOrderId: number;
  statusId: number | null;
  statusCode: string | null;
  statusName: string | null;
  createdByUserId: number;
  createdByUserName: string;
  createdByUserEmail: string;
  comment: string;
  visibility: RepairOrderHistoryVisibility;
  createdAt: string;
}

export type RepairOrderHistoryVisibility = "INTERNAL" | "CUSTOMER";

export interface UpdateRepairOrderStatusRequest {
  statusId: number;
  comment?: string;
  visibility?: RepairOrderHistoryVisibility;
}

export interface RepairOrderAttachment {
  id: number;
  repairOrderId: number;
  uploadedByBusinessUserId: number;
  uploadedByEmail: string;
  uploadedByName: string;
  fileUrl: string;
  originalFileName: string;
  contentType: string;
  fileSize: number;
  checksumSha256: string;
  description: string | null;
  createdAt: string;
}

export interface BusinessTechnicianOption {
  businessUserId: number;
  userId: number;
  userName: string;
  userSurname: string;
  userEmail: string;
  roleCode: string;
  roleName: string;
  commissionPercentage: number | null;
}
