import { describe, expect, it } from "vitest";

import { customerDeviceSchema, nullableTrimmed } from "./schemas";

const validValues = {
  customerId: "5",
  deviceTypeId: "8",
  brandId: "12",
  model: "iPhone 13 Pro",
  serialNumber: "SN-ABC-12345",
  imei: "351756051523999",
  color: "Azul",
};

describe("customerDeviceSchema", () => {
  it("acepta el contrato mínimo válido", () => {
    expect(customerDeviceSchema.safeParse({
      ...validValues,
      serialNumber: "",
      imei: "",
    }).success).toBe(true);
  });

  it("respeta los límites exactos del backend", () => {
    expect(customerDeviceSchema.safeParse({ ...validValues, model: "x".repeat(51) }).success).toBe(false);
    expect(customerDeviceSchema.safeParse({ ...validValues, serialNumber: "x".repeat(61) }).success).toBe(false);
    expect(customerDeviceSchema.safeParse({ ...validValues, color: "x".repeat(21) }).success).toBe(false);
  });
});

describe("nullableTrimmed", () => {
  it("envía como null los identificadores opcionales vacíos", () => {
    expect(nullableTrimmed("   ")).toBeNull();
    expect(nullableTrimmed("  SN-01 ")).toBe("SN-01");
  });
});
