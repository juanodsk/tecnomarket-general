import { describe, expect, it } from "vitest";

import { changePasswordSchema, registerSchema } from "@/features/auth/schemas";

const validRegistration = {
  documentTypeCode: "CC",
  documentNumber: "123456",
  name: "Ana",
  surname: "Pérez",
  email: "ana@example.com",
  password: "password",
  confirmPassword: "password",
  address: "Calle 1",
  phone: "3000000000",
  countryId: "1",
  departmentId: "2",
  cityId: "3",
};

describe("esquemas de identidad", () => {
  it("acepta ocho caracteres en registro", () => {
    expect(registerSchema.safeParse(validRegistration).success).toBe(true);
  });

  it("exige doce caracteres al cambiar contraseña", () => {
    expect(changePasswordSchema.safeParse({ currentPassword: "old-pass", newPassword: "shortpass", confirmPassword: "shortpass" }).success).toBe(false);
  });

  it("rechaza confirmaciones distintas", () => {
    expect(registerSchema.safeParse({ ...validRegistration, confirmPassword: "different" }).success).toBe(false);
  });
});
