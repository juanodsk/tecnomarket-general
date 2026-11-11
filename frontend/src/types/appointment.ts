export interface Appointment {
  id: number;
  businessId: number;
  branchId: number;
  branchName: string;
  customerId: number;
  customerName: string;
  customerSurname: string;
  customerEmail: string;
  customerDeviceId: number;
  customerDeviceLabel: string;
  businessServiceId: number;
  businessServiceName: string;
  statusId: number;
  statusCode: string;
  statusName: string;
  scheduledAt: string;
  problemDescription: string;
  active: boolean;
}

export interface CreateAppointment {
  branchId: number;
  customerId: number;
  customerDeviceId: number;
  businessServiceId: number;
  statusId: number;
  scheduledAt: string;
  problemDescription: string;
}

export interface UpdateAppointment extends Omit<CreateAppointment, "branchId"> {
  active: boolean;
}
