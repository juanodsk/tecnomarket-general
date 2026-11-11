import { describe, expect, it } from "vitest";

import { getQuotationStatusTargets } from "@/features/quotations/transitions";
import type { BusinessCatalogItem } from "@/types/business-service";

const statuses = ["DRAFT", "PENDING", "APPROVED", "REJECTED", "EXPIRED", "CANCELLED"].map((code, index) => ({
  id: index + 1,
  businessId: 10,
  catalogType: "QUOTATION_STATUS" as const,
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

describe("getQuotationStatusTargets", () => {
  it("expone solo transiciones permitidas desde DRAFT", () => {
    expect(getQuotationStatusTargets("DRAFT", statuses).map((status) => status.code)).toEqual(["PENDING", "CANCELLED"]);
  });

  it("expone las transiciones permitidas desde PENDING y excluye estados inactivos", () => {
    const values = statuses.map((status) => status.code === "REJECTED" ? { ...status, active: false } : status);
    expect(getQuotationStatusTargets("PENDING", values).map((status) => status.code)).toEqual(["APPROVED", "EXPIRED", "CANCELLED"]);
  });

  it("no ofrece transiciones desde estados terminales", () => {
    expect(getQuotationStatusTargets("APPROVED", statuses)).toEqual([]);
    expect(getQuotationStatusTargets("REJECTED", statuses)).toEqual([]);
    expect(getQuotationStatusTargets("EXPIRED", statuses)).toEqual([]);
    expect(getQuotationStatusTargets("CANCELLED", statuses)).toEqual([]);
  });
});
