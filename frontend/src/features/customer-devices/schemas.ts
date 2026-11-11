import { z } from "zod";

export const customerDeviceSchema = z.object({
  customerId: z.string().min(1, "Selecciona un cliente."),
  deviceTypeId: z.string().min(1, "Selecciona un tipo de dispositivo."),
  brandId: z.string().min(1, "Selecciona una marca."),
  model: z.string().trim().min(1, "Ingresa el modelo.").max(50, "Usa máximo 50 caracteres."),
  serialNumber: z.string().trim().max(60, "Usa máximo 60 caracteres."),
  imei: z.string().trim().max(60, "Usa máximo 60 caracteres."),
  color: z.string().trim().min(1, "Ingresa el color.").max(20, "Usa máximo 20 caracteres."),
});

export type CustomerDeviceFormValues = z.infer<typeof customerDeviceSchema>;

export function nullableTrimmed(value: string) {
  const normalized = value.trim();
  return normalized.length ? normalized : null;
}
