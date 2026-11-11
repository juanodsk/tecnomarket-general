import { z } from "zod";

const required = "Este campo es obligatorio.";
const optionalDecimal = (label: string, minimum: number, maximum?: number) =>
  z.string().trim().refine((value) => {
    if (!value) return true;
    const parsed = Number(value);
    return Number.isFinite(parsed) && parsed >= minimum && (maximum === undefined || parsed <= maximum);
  }, maximum === undefined ? `${label} debe ser mayor o igual a ${minimum}.` : `${label} debe estar entre ${minimum} y ${maximum}.`);

export const businessBranchSchema = z.object({
  name: z.string().trim().min(1, required).max(120, "Máximo 120 caracteres."),
  address: z.string().trim().min(1, required).max(200, "Máximo 200 caracteres."),
  neighborhood: z.string().trim().max(120, "Máximo 120 caracteres."),
  phone: z.string().trim().max(30, "Máximo 30 caracteres."),
  countryId: z.string(),
  departmentId: z.string(),
  cityId: z.string(),
  latitude: optionalDecimal("La latitud", -90, 90),
  longitude: optionalDecimal("La longitud", -180, 180),
  serviceRadiusKm: optionalDecimal("El radio de servicio", 0.1),
  primaryBranch: z.boolean(),
  changeLocation: z.boolean(),
}).superRefine((values, context) => {
  if (values.changeLocation) {
    if (!values.countryId) context.addIssue({ code: "custom", path: ["countryId"], message: "Selecciona un país." });
    if (!values.departmentId) context.addIssue({ code: "custom", path: ["departmentId"], message: "Selecciona un departamento." });
    if (!values.cityId) context.addIssue({ code: "custom", path: ["cityId"], message: "Selecciona una ciudad." });
  } else if (!values.cityId) {
    context.addIssue({ code: "custom", path: ["cityId"], message: "La sede debe conservar una ciudad." });
  }

  if (Boolean(values.latitude) !== Boolean(values.longitude)) {
    context.addIssue({ code: "custom", path: [values.latitude ? "longitude" : "latitude"], message: "Ingresa latitud y longitud juntas." });
  }
});

export type BusinessBranchFormValues = z.infer<typeof businessBranchSchema>;
