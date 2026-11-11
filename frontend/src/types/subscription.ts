export type SubscriptionStatus = "TRIALING" | "ACTIVE" | "PAST_DUE" | "CANCELLED" | "EXPIRED";
export type BillingCycle = "MONTHLY" | "YEARLY";
export type PaymentProvider = "MANUAL" | "STRIPE" | "WOMPI" | "MERCADOPAGO";

export interface BusinessSubscription {
  id: number;
  businessId: number;
  businessName: string;
  planId: number;
  planCode: string;
  planName: string;
  status: SubscriptionStatus;
  billingCycle: BillingCycle;
  currentPeriodStart: string;
  currentPeriodEnd: string;
  trialEndsAt: string | null;
  cancelAtPeriodEnd: boolean;
  paymentProvider: PaymentProvider;
  providerCustomerId: string | null;
  providerSubscriptionId: string | null;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface SubscriptionUsage {
  id: number;
  businessSubscriptionId: number;
  usersCount: number;
  ordersCount: number;
  branchesCount: number;
  storageUsed: number;
  updatedAt: string;
}

