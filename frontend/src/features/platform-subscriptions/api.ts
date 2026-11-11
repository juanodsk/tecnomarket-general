import { api } from "@/lib/api/client";
import type { ApiResponse } from "@/types/api";
import type { BusinessSubscription, SubscriptionUsage } from "@/types/subscription";

export async function getBusinessSubscriptions() {
  const response = await api.get<ApiResponse<BusinessSubscription[]>>("/api/v1/business-subscriptions");
  return response.data.data;
}

export async function getBusinessSubscription(id: number) {
  const response = await api.get<ApiResponse<BusinessSubscription>>(`/api/v1/business-subscriptions/${id}`);
  return response.data.data;
}

export async function getSubscriptionUsage(id: number) {
  const response = await api.get<ApiResponse<SubscriptionUsage>>(`/api/v1/business-subscriptions/${id}/usage`);
  return response.data.data;
}

export async function recalculateSubscriptionUsage(id: number) {
  const response = await api.post<ApiResponse<SubscriptionUsage>>(`/api/v1/business-subscriptions/${id}/usage/recalculate`);
  return response.data.data;
}

