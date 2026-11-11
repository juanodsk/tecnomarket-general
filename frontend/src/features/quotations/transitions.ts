import type { BusinessCatalogItem } from "@/types/business-service";

// Mirrors QuotationStateMachine in the backend. Status IDs and labels always come from its business catalog.
const ALLOWED_TARGET_CODES: Readonly<Record<string, readonly string[]>> = {
  DRAFT: ["PENDING", "CANCELLED"],
  PENDING: ["APPROVED", "REJECTED", "EXPIRED", "CANCELLED"],
};

export function getQuotationStatusTargets(currentCode: string, statuses: BusinessCatalogItem[]) {
  const allowedCodes = ALLOWED_TARGET_CODES[currentCode.toLocaleUpperCase("en-US")] ?? [];
  return statuses
    .filter((status) => status.active && allowedCodes.includes(status.code.toLocaleUpperCase("en-US")))
    .sort((left, right) => (left.sortOrder ?? 0) - (right.sortOrder ?? 0));
}
