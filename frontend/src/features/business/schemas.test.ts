import { describe, expect, it } from "vitest";

import { businessRequestSchema } from "@/features/business/schemas";

const validRequest = {
  documentTypeCode: "NIT",
  documentNumber: "901234567-1",
  businessName: "Tecno Centro",
  legalName: "Tecno Centro S.A.S.",
  email: "contacto@tecnocentro.com",
  phone: "+573001112233",
  countryId: "1",
  departmentId: "2",
  cityId: "3",
  address: "Calle 10 # 2-30",
  legalFile: "https://archivos.example.com/camara-comercio.pdf",
};

describe("businessRequestSchema", () => {
  it("acepta el contrato completo de una solicitud", () => {
    expect(businessRequestSchema.safeParse(validRequest).success).toBe(true);
  });

  it("mantiene opcionales la razón social y la dirección", () => {
    expect(businessRequestSchema.safeParse({ ...validRequest, legalName: "", address: "" }).success).toBe(true);
  });

  it("rechaza campos que exceden los límites del backend", () => {
    const result = businessRequestSchema.safeParse({ ...validRequest, documentNumber: "1".repeat(51) });
    expect(result.success).toBe(false);
  });
});
