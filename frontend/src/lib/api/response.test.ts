import { describe, expect, it } from "vitest";

import { unwrapApiResponse } from "@/lib/api/response";

describe("unwrapApiResponse", () => {
  it("devuelve los datos de una respuesta exitosa", () => {
    expect(unwrapApiResponse({ success: true, message: "ok", data: { id: 7 } })).toEqual({ id: 7 });
  });

  it("rechaza una envoltura marcada como fallida", () => {
    expect(() => unwrapApiResponse({ success: false, message: "Conflicto", data: null })).toThrow("Conflicto");
  });
});
