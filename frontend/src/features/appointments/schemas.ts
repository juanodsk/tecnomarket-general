import { z } from "zod";

export const appointmentSchema = z.object({
  branchId: z.string().min(1, "Selecciona una sede."),
  customerId: z.string().min(1, "Selecciona un cliente."),
  customerDeviceId: z.string().min(1, "Selecciona un dispositivo."),
  businessServiceId: z.string().min(1, "Selecciona un servicio."),
  statusId: z.string().min(1, "Selecciona un estado."),
  scheduledAt: z.string().min(1, "Selecciona la fecha y hora."),
  problemDescription: z
    .string()
    .trim()
    .min(1, "Describe el motivo de la cita.")
    .max(200, "Usa máximo 200 caracteres."),
});

export type AppointmentFormValues = z.infer<typeof appointmentSchema>;

export function normalizeScheduledAt(value: string) {
  return value.length === 16 ? `${value}:00` : value;
}

export function toDateTimeLocal(value: string) {
  return value.length >= 16 ? value.slice(0, 16) : value;
}
