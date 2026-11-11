import { describe, expect, it } from "vitest";

import { businessScheduleSchema, schedulesOverlap, toApiTime } from "./schemas";

describe("businessScheduleSchema", () => {
  it("acepta una jornada válida", () => {
    expect(businessScheduleSchema.safeParse({ openTime: "08:00", closeTime: "12:00" }).success).toBe(true);
  });

  it("rechaza jornadas invertidas o sin duración", () => {
    expect(businessScheduleSchema.safeParse({ openTime: "12:00", closeTime: "08:00" }).success).toBe(false);
    expect(businessScheduleSchema.safeParse({ openTime: "08:00", closeTime: "08:00" }).success).toBe(false);
  });
});

describe("schedulesOverlap", () => {
  it("detecta solapamientos reales", () => {
    expect(schedulesOverlap("11:00", "13:00", "08:00:00", "12:00:00")).toBe(true);
  });

  it("permite jornadas consecutivas", () => {
    expect(schedulesOverlap("12:00", "14:00", "08:00:00", "12:00:00")).toBe(false);
    expect(toApiTime("08:30")).toBe("08:30:00");
  });
});
