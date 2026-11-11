import { z } from "zod";

const required = "Este campo es obligatorio.";

export const businessRequestSchema = z.object({
  documentTypeCode: z.string().min(1, "Selecciona un tipo de documento.").max(15),
  documentNumber: z.string().trim().min(1, required).max(50, "Máximo 50 caracteres."),
  businessName: z.string().trim().min(1, required).max(150, "Máximo 150 caracteres."),
  legalName: z.string().trim().max(200, "Máximo 200 caracteres."),
  email: z.string().trim().min(1, required).email("Ingresa un correo válido.").max(150),
  phone: z.string().trim().min(1, required).max(50, "Máximo 50 caracteres."),
  countryId: z.string().min(1, "Selecciona un país."),
  departmentId: z.string().min(1, "Selecciona un departamento."),
  cityId: z.string().min(1, "Selecciona una ciudad."),
  address: z.string().trim().max(200, "Máximo 200 caracteres."),
  legalFile: z.string().trim().min(1, required).max(500, "Máximo 500 caracteres."),
});

export type BusinessRequestFormValues = z.infer<typeof businessRequestSchema>;
