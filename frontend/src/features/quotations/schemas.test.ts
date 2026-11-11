import { describe, expect, it } from "vitest";

import { quotationDraftSchema } from "@/features/quotations/schemas";

const draft = {
  notes: "Válida por cinco días.",
  expiresAt: "2026-09-30T17:00",
  items: [{ description: "Cambio de pantalla", quantity: "1", unitPrice: "200000.00" }],
};

describe("quotationDraftSchema", () => {
  it("acepta los campos del contrato de creación", () => {
    expect(quotationDraftSchema.safeParse(draft).success).toBe(true);
  });

  it("acepta coma decimal y aplica límites de longitud", () => {
    expect(quotationDraftSchema.safeParse({ ...draft, items: [{ ...draft.items[0], unitPrice: "200000,50" }] }).success).toBe(true);
    expect(quotationDraftSchema.safeParse({ ...draft, notes: "N".repeat(501) }).success).toBe(false);
    expect(quotationDraftSchema.safeParse({ ...draft, items: [{ ...draft.items[0], description: "D".repeat(301) }] }).success).toBe(false);
  });

  it("requiere al menos un concepto, cantidades enteras y precios positivos", () => {
    expect(quotationDraftSchema.safeParse({ ...draft, items: [] }).success).toBe(false);
    expect(quotationDraftSchema.safeParse({ ...draft, items: [{ ...draft.items[0], quantity: "0" }] }).success).toBe(false);
    expect(quotationDraftSchema.safeParse({ ...draft, items: [{ ...draft.items[0], quantity: "1.5" }] }).success).toBe(false);
    expect(quotationDraftSchema.safeParse({ ...draft, items: [{ ...draft.items[0], unitPrice: "0" }] }).success).toBe(false);
  });
});
