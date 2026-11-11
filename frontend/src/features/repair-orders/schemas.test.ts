import { describe, expect, it } from "vitest";

import {
  normalizeOptionalDateTime,
  normalizeOptionalText,
  validateRepairOrderEvidence,
  repairOrderSchema,
} from "@/features/repair-orders/schemas";

const validValues = {
  branchId: "1",
  customerId: "2",
  customerDeviceId: "3",
  appointmentId: "",
  businessServiceId: "4",
  assignedBusinessUserId: "5",
  problemDescription: "No enciende",
  diagnosis: "",
  estimatedPrice: "150000",
  estimatedDeliveryAt: "",
  receivedAt: "",
};

describe("repairOrderSchema", () => {
  it("acepta una orden válida con campos opcionales vacíos", () => {
    expect(repairOrderSchema.safeParse(validValues).success).toBe(true);
  });

  it("exige sede, cliente, dispositivo, servicio, técnico y problema", () => {
    const parsed = repairOrderSchema.safeParse({
      ...validValues,
      branchId: "",
      customerId: "",
      customerDeviceId: "",
      businessServiceId: "",
      assignedBusinessUserId: "",
      problemDescription: " ",
    });

    expect(parsed.success).toBe(false);
    if (!parsed.success) expect(parsed.error.issues).toHaveLength(6);
  });

  it("rechaza precios negativos", () => {
    expect(repairOrderSchema.safeParse({ ...validValues, estimatedPrice: "-1" }).success).toBe(false);
  });
});

describe("normalizadores de orden", () => {
  it("convierte cadenas vacías en null", () => {
    expect(normalizeOptionalText("  ")).toBeNull();
    expect(normalizeOptionalDateTime("")).toBeNull();
  });

  it("completa segundos en datetime-local", () => {
    expect(normalizeOptionalDateTime("2026-09-23T14:30")).toBe("2026-09-23T14:30:00");
  });
});

describe("validación de evidencias fotográficas", () => {
  it.each(["image/jpeg", "image/png", "image/webp"])("acepta %s", (type) => {
    expect(validateRepairOrderEvidence(new File(["image"], "evidencia", { type }))).toBeNull();
  });

  it("rechaza formatos distintos de imagen permitida", () => {
    expect(validateRepairOrderEvidence(new File(["document"], "evidencia.pdf", { type: "application/pdf" }))).toContain("JPG, PNG o WebP");
  });

  it("rechaza archivos de más de 10 MB", () => {
    const oversized = new File([new Uint8Array(10 * 1024 * 1024 + 1)], "evidencia.jpg", { type: "image/jpeg" });
    expect(validateRepairOrderEvidence(oversized)).toContain("10 MB");
  });
});
