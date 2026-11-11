import { describe, expect, it } from "vitest";

import { getRepairOrderStatusTargets } from "@/features/repair-orders/transitions";
import type { BusinessCatalogItem } from "@/types/business-service";

const statuses = ["RECEIVED", "DIAGNOSIS", "WAITING_APPROVAL", "WAITING_PARTS", "REPAIRING", "TESTING", "READY_FOR_PICKUP", "DELIVERED", "CANCELLED"].map((code, index) => ({
  id: index + 1,
  businessId: 10,
  catalogType: "REPAIR_ORDER_STATUS" as const,
  code,
  name: `Estado ${code}`,
  description: null,
  icon: null,
  sortOrder: index,
  protectedItem: true,
  active: true,
  createdAt: "2026-01-01T00:00:00",
  updatedAt: "2026-01-01T00:00:00",
})) satisfies BusinessCatalogItem[];

const updateAndClose = { canUpdate: true, canClose: true, hasFinalPrice: true };

describe("getRepairOrderStatusTargets", () => {
  it("matches backend transitions and keeps the business catalog ordering", () => {
    expect(getRepairOrderStatusTargets("RECEIVED", statuses, updateAndClose).map((status) => status.code)).toEqual(["DIAGNOSIS", "CANCELLED"]);
    expect(getRepairOrderStatusTargets("WAITING_APPROVAL", statuses, updateAndClose).map((status) => status.code)).toEqual(["WAITING_PARTS", "REPAIRING", "CANCELLED"]);
    expect(getRepairOrderStatusTargets("TESTING", statuses, updateAndClose).map((status) => status.code)).toEqual(["REPAIRING", "READY_FOR_PICKUP", "CANCELLED"]);
  });

  it("requires REPAIR_ORDER_UPDATE for operational states and REPAIR_ORDER_CLOSE for terminal states", () => {
    expect(getRepairOrderStatusTargets("RECEIVED", statuses, { ...updateAndClose, canUpdate: false }).map((status) => status.code)).toEqual(["CANCELLED"]);
    expect(getRepairOrderStatusTargets("RECEIVED", statuses, { ...updateAndClose, canClose: false }).map((status) => status.code)).toEqual(["DIAGNOSIS"]);
    expect(getRepairOrderStatusTargets("READY_FOR_PICKUP", statuses, { ...updateAndClose, canClose: false }).map((status) => status.code)).toEqual([]);
  });

  it("does not offer delivery before a final price is recorded", () => {
    const targets = getRepairOrderStatusTargets("READY_FOR_PICKUP", statuses, { ...updateAndClose, hasFinalPrice: false });
    expect(targets.map((status) => status.code)).toEqual(["CANCELLED"]);
  });

  it("excludes inactive or unknown statuses and offers no transition from terminal states", () => {
    const catalog = statuses.map((status) => status.code === "DIAGNOSIS" ? { ...status, active: false } : status);
    expect(getRepairOrderStatusTargets("RECEIVED", catalog, updateAndClose).map((status) => status.code)).toEqual(["CANCELLED"]);
    expect(getRepairOrderStatusTargets("DELIVERED", statuses, updateAndClose)).toEqual([]);
    expect(getRepairOrderStatusTargets("CANCELLED", statuses, updateAndClose)).toEqual([]);
    expect(getRepairOrderStatusTargets("OTHER", statuses, updateAndClose)).toEqual([]);
  });
});
