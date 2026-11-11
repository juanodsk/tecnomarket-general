import type { ApiResponse } from "@/types/api";

export function unwrapApiResponse<T>(response: ApiResponse<T>): T {
  if (!response.success) {
    throw new Error(response.message || "La API indicó que la operación no fue exitosa.");
  }

  return response.data;
}
