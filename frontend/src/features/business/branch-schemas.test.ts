import { describe, expect, it } from "vitest";

import { businessBranchSchema } from "@/features/business/branch-schemas";

const validBranch = {
  name: "Sede Norte",
  address: "Carrera 15 # 100-20",
  neighborhood: "Usaquén",
  phone: "+573001112233",
  countryId: "1",
  departmentId: "2",
  cityId: "3",
  latitude: "4.6845",
  longitude: "-74.0478",
  serviceRadiusKm: "8",
  primaryBranch: false,
  changeLocation: true,
};

describe("businessBranchSchema", () => {
  it("acepta el contrato completo de una sede", () => {
    expect(businessBranchSchema.safeParse(validBranch).success).toBe(true);
  });

  it("permite conservar la ciudad actual al editar", () => {
    expect(businessBranchSchema.safeParse({
      ...validBranch,
      countryId: "",
      departmentId: "",
      changeLocation: false,
    }).success).toBe(true);
  });

  it("exige las dos coordenadas como pareja", () => {
    expect(businessBranchSchema.safeParse({ ...validBranch, longitude: "" }).success).toBe(false);
  });

  it("respeta límites y longitudes del backend", () => {
    expect(businessBranchSchema.safeParse({ ...validBranch, latitude: "91" }).success).toBe(false);
    expect(businessBranchSchema.safeParse({ ...validBranch, name: "S".repeat(121) }).success).toBe(false);
  });
});
