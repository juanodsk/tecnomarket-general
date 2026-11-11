import type { BusinessCatalogItem } from "@/types/business-service";

interface RepairOrderTransitionAccess {
  canUpdate: boolean;
  canClose: boolean;
  hasFinalPrice: boolean;
}

// Mirrors RepairOrderStateMachine and RepairOrderService in the backend.
const ALLOWED_TARGET_CODES: Readonly<Record<string, readonly string[]>> = {
  RECEIVED: ["DIAGNOSIS", "CANCELLED"],
  DIAGNOSIS: ["WAITING_APPROVAL", "CANCELLED"],
  WAITING_APPROVAL: ["WAITING_PARTS", "REPAIRING", "CANCELLED"],
  WAITING_PARTS: ["REPAIRING", "CANCELLED"],
  REPAIRING: ["WAITING_PARTS", "TESTING", "CANCELLED"],
  TESTING: ["REPAIRING", "READY_FOR_PICKUP", "CANCELLED"],
  READY_FOR_PICKUP: ["DELIVERED", "CANCELLED"],
};

const CLOSE_TARGET_CODES = new Set(["DELIVERED", "CANCELLED"]);

export function getRepairOrderStatusTargets(
  currentCode: string,
  statuses: BusinessCatalogItem[],
  access: RepairOrderTransitionAccess,
) {
  const allowedCodes = ALLOWED_TARGET_CODES[currentCode.trim().toUpperCase()] ?? [];

  return statuses
    .filter((status) => {
      const code = status.code.trim().toUpperCase();
      if (!status.active || !allowedCodes.includes(code)) return false;
      if (CLOSE_TARGET_CODES.has(code) ? !access.canClose : !access.canUpdate) return false;
      if (code === "DELIVERED" && !access.hasFinalPrice) return false;
      return true;
    })
    .sort((left, right) => (left.sortOrder ?? 0) - (right.sortOrder ?? 0));
}
