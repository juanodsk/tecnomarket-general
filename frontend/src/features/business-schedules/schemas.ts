import { z } from "zod";

const timePattern = /^([01]\d|2[0-3]):[0-5]\d$/;

export const businessScheduleSchema = z
  .object({
    openTime: z.string().regex(timePattern, "Selecciona una hora de apertura válida."),
    closeTime: z.string().regex(timePattern, "Selecciona una hora de cierre válida."),
  })
  .refine(({ openTime, closeTime }) => openTime < closeTime, {
    path: ["closeTime"],
    message: "La hora de cierre debe ser posterior a la apertura.",
  });

export type BusinessScheduleFormValues = z.infer<typeof businessScheduleSchema>;

function minutes(time: string) {
  const [hours = 0, minutesValue = 0] = time.slice(0, 5).split(":").map(Number);
  return hours * 60 + minutesValue;
}

export function schedulesOverlap(
  openTime: string,
  closeTime: string,
  existingOpenTime: string,
  existingCloseTime: string,
) {
  return minutes(openTime) < minutes(existingCloseTime)
    && minutes(existingOpenTime) < minutes(closeTime);
}

export function toApiTime(time: string) {
  return time.length === 5 ? `${time}:00` : time;
}
