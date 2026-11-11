import { z } from "zod";

const positiveDecimal = /^\d+(?:[.,]\d{1,2})?$/;

export const businessServiceSchema = z.object({
  serviceCategoryId: z.string().min(1, "Selecciona una categoría."),
  deviceTypeId: z.string().min(1, "Selecciona un tipo de dispositivo."),
  name: z.string().trim().min(1, "Ingresa el nombre del servicio.").max(50, "Usa máximo 50 caracteres."),
  description: z.string().trim().min(1, "Describe qué incluye el servicio.").max(200, "Usa máximo 200 caracteres."),
  basePrice: z.string()
    .trim()
    .regex(positiveDecimal, "Ingresa un valor válido con máximo dos decimales.")
    .refine((value) => Number(value.replace(",", ".")) > 0, "El precio debe ser mayor que cero."),
  estimatedTimeMinutes: z.string()
    .trim()
    .regex(/^\d+$/, "Ingresa el tiempo en minutos completos.")
    .refine((value) => Number(value) > 0, "El tiempo debe ser mayor que cero."),
});

export type BusinessServiceFormValues = z.infer<typeof businessServiceSchema>;

export const SERVICE_IMAGE_TYPES = ["image/jpeg", "image/png", "image/webp"] as const;
export const SERVICE_IMAGE_MAX_BYTES = 10 * 1024 * 1024;

export function validateServiceImage(file: File) {
  if (!SERVICE_IMAGE_TYPES.includes(file.type as (typeof SERVICE_IMAGE_TYPES)[number])) {
    return "Usa una imagen JPG, PNG o WebP.";
  }
  if (file.size > SERVICE_IMAGE_MAX_BYTES) {
    return "La imagen no puede superar 10 MB.";
  }
  return null;
}
