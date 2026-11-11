import { describe, expect, it } from "vitest";

import {
  businessUserAssignmentSchema,
  businessUserEmailSchema,
} from "@/features/business/business-user-schemas";

describe("businessUserEmailSchema", () => {
  it("acepta un correo exacto válido", () => {
    expect(businessUserEmailSchema.safeParse("tecnico@taller.com").success).toBe(true);
  });

  it("rechaza búsquedas que no son correos", () => {
    expect(businessUserEmailSchema.safeParse("tecnico").success).toBe(false);
  });
});

describe("businessUserAssignmentSchema", () => {
  it("requiere rol y al menos una sede", () => {
    const result = businessUserAssignmentSchema.safeParse({
      roleId: "",
      branchIds: [],
      commissionPercentage: "",
    });

    expect(result.success).toBe(false);
  });

  it("acepta una comisión con dos decimales", () => {
    const result = businessUserAssignmentSchema.safeParse({
      roleId: "4",
      branchIds: ["1", "2"],
      commissionPercentage: "20,50",
    });

    expect(result.success).toBe(true);
  });

  it("rechaza porcentajes fuera del contrato", () => {
    const result = businessUserAssignmentSchema.safeParse({
      roleId: "4",
      branchIds: ["1"],
      commissionPercentage: "100.01",
    });

    expect(result.success).toBe(false);
  });
});
