import { describe, expect, it } from "vitest";

import {
  appointmentSchema,
  normalizeScheduledAt,
  toDateTimeLocal,
} from "./schemas";

const validValues = {
  branchId: "1",
  customerId: "5",
  customerDeviceId: "2",
  businessServiceId: "3",
  statusId: "4",
  scheduledAt: "2026-05-20T09:00",
  problemDescription: "El equipo no enciende.",
};

describe("appointmentSchema", () => {
  it("acepta el contrato mínimo válido", () => {
    expect(appointmentSchema.safeParse(validValues).success).toBe(true);
  });

  it("exige todas las relaciones del contrato", () => {
    expect(appointmentSchema.safeParse({ ...validValues, customerDeviceId: "" }).success).toBe(false);
    expect(appointmentSchema.safeParse({ ...validValues, statusId: "" }).success).toBe(false);
  });

  it("respeta el límite exacto de la descripción", () => {
    expect(appointmentSchema.safeParse({ ...validValues, problemDescription: "x".repeat(200) }).success).toBe(true);
    expect(appointmentSchema.safeParse({ ...validValues, problemDescription: "x".repeat(201) }).success).toBe(false);
  });
});

describe("appointment date helpers", () => {
  it("envía segundos al backend sin introducir una zona horaria", () => {
    expect(normalizeScheduledAt("2026-05-20T09:00")).toBe("2026-05-20T09:00:00");
  });

  it("adapta la respuesta al control datetime-local", () => {
    expect(toDateTimeLocal("2026-05-20T09:00:00")).toBe("2026-05-20T09:00");
  });
});
