export interface QuotationItem {
  id: number;
  description: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface Quotation {
  id: number;
  businessId: number;
  repairOrderId: number;
  customerId: number;
  customerName: string;
  statusCode: string;
  statusName: string;
  totalAmount: number;
  notes: string | null;
  expiresAt: string | null;
  createdAt: string;
  items: QuotationItem[];
}

export interface CreateQuotationItem {
  description: string;
  quantity: number;
  unitPrice: number;
}

export interface CreateQuotationRequest {
  repairOrderId: number;
  notes?: string;
  expiresAt?: string;
  items: CreateQuotationItem[];
}

export interface UpdateQuotationStatusRequest {
  statusId: number;
  notes?: string;
}
