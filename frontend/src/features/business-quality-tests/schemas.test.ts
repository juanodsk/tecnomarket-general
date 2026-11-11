import { describe, expect, it } from "vitest";

import {
  businessQualityTestSchema,
  hasDuplicateQualityTestCode,
  normalizeQualityTestCode,
} from "./schemas";

const validValues = {
  deviceTypeId: "8",
  code: "screen_ok",
  name: "Pantalla funciona",
  description: "Comprueba touch, brillo y visualización.",
  sortOrder: "1",
  required: true,
};

describe("businessQualityTestSchema", () => {
  it("acepta el contrato mínimo válido", () => {
    expect(businessQualityTestSchema.safeParse(validValues).success).toBe(true);
  });

  it("respeta límites y orden mínimo", () => {
    expect(businessQualityTestSchema.safeParse({ ...validValues, code: "x".repeat(31) }).success).toBe(false);
    expect(businessQualityTestSchema.safeParse({ ...validValues, sortOrder: "0" }).success).toBe(false);
  });
});

describe("quality test codes", () => {
  it("normaliza el código como el backend", () => {
    expect(normalizeQualityTestCode("  screen_ok ")).toBe("SCREEN_OK");
  });

  it("detecta duplicados únicamente dentro del mismo tipo de dispositivo", () => {
    const tests = [{ id: 1, deviceTypeId: 8, code: "SCREEN_OK" }];
    expect(hasDuplicateQualityTestCode(tests, 8, "screen_ok")).toBe(true);
    expect(hasDuplicateQualityTestCode(tests, 9, "screen_ok")).toBe(false);
    expect(hasDuplicateQualityTestCode(tests, 8, "screen_ok", 1)).toBe(false);
  });
});
