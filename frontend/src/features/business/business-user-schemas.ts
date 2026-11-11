import { z } from "zod";

const required = "Este campo es obligatorio.";

export const businessUserEmailSchema = z
  .string()
  .trim()
  .min(1, "Ingresa el correo del usuario.")
  .email("Ingresa un correo válido.");

export const businessUserAssignmentSchema = z.object({
  roleId: z.string().min(1, "Selecciona un rol."),
  branchIds: z.array(z.string()).min(1, "Selecciona al menos una sede."),
  commissionPercentage: z
    .string()
    .trim()
    .refine((value) => {
      if (!value) return true;
      if (!/^\d{1,3}(?:[.,]\d{1,2})?$/.test(value)) return false;
      const parsed = Number(value.replace(",", "."));
      return Number.isFinite(parsed) && parsed >= 0 && parsed <= 100;
    }, "La comisión debe estar entre 0 y 100, con máximo dos decimales."),
}).refine((values) => values.roleId.trim().length > 0, {
  path: ["roleId"],
  message: required,
});

export type BusinessUserAssignmentValues = z.infer<typeof businessUserAssignmentSchema>;
