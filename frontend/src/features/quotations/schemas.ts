import { z } from "zod";

const positiveDecimal = /^\d+(?:[.,]\d{1,2})?$/;

export const quotationItemDraftSchema = z.object({
  description: z.string().trim().min(1, "Describe el trabajo o repuesto.").max(300, "Usa máximo 300 caracteres."),
  quantity: z.string().trim().regex(/^\d+$/, "Ingresa una cantidad entera.").refine((value) => Number(value) > 0, "La cantidad debe ser mayor que cero."),
  unitPrice: z.string().trim().regex(positiveDecimal, "Ingresa un valor válido con máximo dos decimales.").refine((value) => Number(value.replace(",", ".")) > 0, "El precio debe ser mayor que cero."),
});

export const quotationDraftSchema = z.object({
  notes: z.string().max(500, "Usa máximo 500 caracteres."),
  expiresAt: z.string(),
  items: z.array(quotationItemDraftSchema).min(1, "Agrega al menos un concepto.").max(100, "La cotización admite máximo 100 conceptos."),
});

export type QuotationDraftValues = z.infer<typeof quotationDraftSchema>;
