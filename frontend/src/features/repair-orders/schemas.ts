import { z } from "zod";

export const repairOrderSchema = z.object({
  branchId: z.string().min(1, "Selecciona una sede."),
  customerId: z.string().min(1, "Selecciona un cliente."),
  customerDeviceId: z.string().min(1, "Selecciona un dispositivo."),
  appointmentId: z.string(),
  businessServiceId: z.string().min(1, "Selecciona un servicio."),
  assignedBusinessUserId: z.string().min(1, "Selecciona un técnico."),
  problemDescription: z
    .string()
    .trim()
    .min(1, "Describe el problema reportado.")
    .max(2000, "Usa máximo 2000 caracteres."),
  diagnosis: z.string().trim().max(2000, "Usa máximo 2000 caracteres."),
  estimatedPrice: z
    .string()
    .refine((value) => value === "" || (!Number.isNaN(Number(value)) && Number(value) >= 0), {
      message: "Ingresa un valor igual o mayor a cero.",
    }),
  estimatedDeliveryAt: z.string(),
  receivedAt: z.string(),
});

export type RepairOrderFormValues = z.infer<typeof repairOrderSchema>;

export const REPAIR_ORDER_EVIDENCE_TYPES = ["image/jpeg", "image/png", "image/webp"] as const;
export const REPAIR_ORDER_EVIDENCE_MAX_BYTES = 10 * 1024 * 1024;
export const REPAIR_ORDER_EVIDENCE_DESCRIPTION_MAX_LENGTH = 500;

export function validateRepairOrderEvidence(file: File) {
  if (!REPAIR_ORDER_EVIDENCE_TYPES.includes(file.type as (typeof REPAIR_ORDER_EVIDENCE_TYPES)[number])) {
    return "Selecciona una imagen JPG, PNG o WebP.";
  }
  if (file.size > REPAIR_ORDER_EVIDENCE_MAX_BYTES) {
    return "La imagen no puede superar 10 MB.";
  }
  return null;
}

export function normalizeOptionalDateTime(value: string) {
  if (!value) return null;
  return value.length === 16 ? `${value}:00` : value;
}

export function normalizeOptionalText(value: string) {
  const normalized = value.trim();
  return normalized || null;
}
