import { z } from "zod";

const requiredId = (message: string) =>
  z.string().min(1, message).refine((value) => Number.isInteger(Number(value)) && Number(value) > 0, message);

export const quickRepairOrderSchema = z.object({
  documentTypeCode: z.string().min(1, "Selecciona un tipo de documento.").max(15),
  documentNumber: z.string().trim().min(1, "Ingresa el número de documento.").max(30, "Usa máximo 30 caracteres."),
  name: z.string().trim().min(1, "Ingresa el nombre.").max(50, "Usa máximo 50 caracteres."),
  surname: z.string().trim().min(1, "Ingresa el apellido.").max(50, "Usa máximo 50 caracteres."),
  email: z.string().trim().min(1, "Ingresa el correo.").email("Ingresa un correo válido.").max(150, "Usa máximo 150 caracteres."),
  address: z.string().trim().min(1, "Ingresa la dirección.").max(200, "Usa máximo 200 caracteres."),
  phone: z.string().trim().min(1, "Ingresa el teléfono.").max(30, "Usa máximo 30 caracteres."),
  countryId: requiredId("Selecciona un país."),
  departmentId: requiredId("Selecciona un departamento."),
  cityId: requiredId("Selecciona una ciudad."),
  deviceTypeId: requiredId("Selecciona un tipo de equipo."),
  brandId: requiredId("Selecciona una marca."),
  model: z.string().trim().min(1, "Ingresa el modelo.").max(50, "Usa máximo 50 caracteres."),
  serialNumber: z.string().max(60, "Usa máximo 60 caracteres."),
  imei: z.string().max(60, "Usa máximo 60 caracteres."),
  color: z.string().trim().min(1, "Ingresa el color.").max(20, "Usa máximo 20 caracteres."),
  branchId: requiredId("Selecciona una sede."),
  businessServiceId: requiredId("Selecciona un servicio."),
  assignedBusinessUserId: requiredId("Selecciona un técnico."),
  problemDescription: z.string().trim().min(1, "Describe el problema reportado.").max(2000, "Usa máximo 2000 caracteres."),
  diagnosis: z.string().max(2000, "Usa máximo 2000 caracteres."),
  estimatedPrice: z.string().refine(
    (value) => value === "" || (!Number.isNaN(Number(value)) && Number(value) >= 0),
    "Ingresa un valor igual o mayor a cero.",
  ),
  registeredAt: z.string(),
  receivedAt: z.string(),
  estimatedDeliveryAt: z.string(),
});

export type QuickRepairOrderFormValues = z.infer<typeof quickRepairOrderSchema>;

export const quickRepairOrderDefaultValues: QuickRepairOrderFormValues = {
  documentTypeCode: "",
  documentNumber: "",
  name: "",
  surname: "",
  email: "",
  address: "",
  phone: "",
  countryId: "",
  departmentId: "",
  cityId: "",
  deviceTypeId: "",
  brandId: "",
  model: "",
  serialNumber: "",
  imei: "",
  color: "",
  branchId: "",
  businessServiceId: "",
  assignedBusinessUserId: "",
  problemDescription: "",
  diagnosis: "",
  estimatedPrice: "",
  registeredAt: "",
  receivedAt: "",
  estimatedDeliveryAt: "",
};
