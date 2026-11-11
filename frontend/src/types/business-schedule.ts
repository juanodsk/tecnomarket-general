export interface BusinessSchedule {
  id: number;
  businessId: number;
  branchId: number;
  branchName: string;
  dayOfWeek: number;
  openTime: string;
  closeTime: string;
  slotOrder: number | null;
  active: boolean;
}

export interface CreateBusinessSchedule {
  branchId: number;
  dayOfWeek: number;
  openTime: string;
  closeTime: string;
  slotOrder: number | null;
}

export interface UpdateBusinessSchedule {
  dayOfWeek: number;
  openTime: string;
  closeTime: string;
  slotOrder: number | null;
  active: boolean;
}
