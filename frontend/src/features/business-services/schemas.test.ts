import { describe, expect, it } from "vitest";

import { businessServiceSchema, validateServiceImage } from "@/features/business-services/schemas";

const validService = {
  serviceCategoryId: "1",
  deviceTypeId: "2",
  name: "Cambio de pantalla OLED",
  description: "Reemplazo y prueba del módulo de pantalla.",
  basePrice: "350000.00",
  estimatedTimeMinutes: "120",
};

describe("businessServiceSchema", () => {
  it("acepta el contrato de creación del backend", () => {
    expect(businessServiceSchema.safeParse(validService).success).toBe(true);
  });

  it("acepta coma decimal sin modificar el contrato enviado", () => {
    expect(businessServiceSchema.safeParse({ ...validService, basePrice: "350000,50" }).success).toBe(true);
  });

  it("respeta longitudes y valores positivos", () => {
    expect(businessServiceSchema.safeParse({ ...validService, name: "S".repeat(51) }).success).toBe(false);
    expect(businessServiceSchema.safeParse({ ...validService, basePrice: "0" }).success).toBe(false);
    expect(businessServiceSchema.safeParse({ ...validService, estimatedTimeMinutes: "1.5" }).success).toBe(false);
  });
});

describe("validateServiceImage", () => {
  it("acepta imágenes soportadas dentro del límite", () => {
    expect(validateServiceImage(new File(["image"], "servicio.webp", { type: "image/webp" }))).toBeNull();
  });

  it("rechaza formatos no soportados", () => {
    expect(validateServiceImage(new File(["image"], "servicio.gif", { type: "image/gif" }))).toBeTruthy();
  });
});
