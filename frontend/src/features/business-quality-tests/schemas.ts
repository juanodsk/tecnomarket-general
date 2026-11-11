import { z } from "zod";

export const businessQualityTestSchema = z.object({
  deviceTypeId: z.string().min(1, "Selecciona un tipo de dispositivo."),
  code: z.string().trim().min(1, "Ingresa el código interno.").max(30, "Usa máximo 30 caracteres."),
  name: z.string().trim().min(1, "Ingresa el nombre de la prueba.").max(50, "Usa máximo 50 caracteres."),
  description: z.string().trim().min(1, "Describe qué debe comprobarse.").max(200, "Usa máximo 200 caracteres."),
  sortOrder: z.string()
    .trim()
    .regex(/^\d+$/, "Ingresa un número entero.")
    .refine((value) => Number(value) >= 1, "El orden debe ser mayor o igual a 1."),
  required: z.boolean(),
});

export type BusinessQualityTestFormValues = z.infer<typeof businessQualityTestSchema>;

export function normalizeQualityTestCode(code: string) {
  return code.trim().toUpperCase();
}

export function hasDuplicateQualityTestCode(
  tests: Array<{ id: number; deviceTypeId: number; code: string }>,
  deviceTypeId: number,
  code: string,
  currentId?: number,
) {
  const normalizedCode = normalizeQualityTestCode(code);
  return tests.some((test) =>
    test.id !== currentId
    && test.deviceTypeId === deviceTypeId
    && normalizeQualityTestCode(test.code) === normalizedCode,
  );
}
