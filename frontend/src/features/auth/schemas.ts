import { z } from "zod";

const required = "Este campo es obligatorio.";
const email = z.string().trim().min(1, required).email("Ingresa un correo válido.").max(150);

export const loginSchema = z.object({
  email,
  password: z.string().min(1, required),
});

export const registerSchema = z
  .object({
    documentTypeCode: z.string().min(1, required).max(15),
    documentNumber: z.string().trim().min(1, required).max(30),
    name: z.string().trim().min(1, required).max(50),
    surname: z.string().trim().min(1, required).max(50),
    email,
    password: z.string().min(8, "Usa al menos 8 caracteres.").max(72),
    confirmPassword: z.string().min(1, required),
    address: z.string().trim().min(1, required).max(200),
    phone: z.string().trim().min(1, required).max(30),
    countryId: z.string().min(1, "Selecciona un país."),
    departmentId: z.string().min(1, "Selecciona un departamento."),
    cityId: z.string().min(1, "Selecciona una ciudad."),
  })
  .refine((values) => values.password === values.confirmPassword, {
    message: "Las contraseñas no coinciden.",
    path: ["confirmPassword"],
  });

export const forgotPasswordSchema = z.object({ email });

export const resetPasswordSchema = z
  .object({
    token: z.string().min(32).max(256),
    newPassword: z.string().min(12, "Usa al menos 12 caracteres.").max(72),
    confirmPassword: z.string().min(1, required),
  })
  .refine((values) => values.newPassword === values.confirmPassword, {
    message: "Las contraseñas no coinciden.",
    path: ["confirmPassword"],
  });

export const changePasswordSchema = z
  .object({
    currentPassword: z.string().min(1, required).max(72),
    newPassword: z.string().min(12, "Usa al menos 12 caracteres.").max(72),
    confirmPassword: z.string().min(1, required),
  })
  .refine((values) => values.newPassword === values.confirmPassword, {
    message: "Las contraseñas no coinciden.",
    path: ["confirmPassword"],
  });

export const verifyEmailTokenSchema = z.string().min(64).max(128);

export type LoginFormValues = z.infer<typeof loginSchema>;
export type RegisterFormValues = z.infer<typeof registerSchema>;
export type ForgotPasswordFormValues = z.infer<typeof forgotPasswordSchema>;
export type ResetPasswordFormValues = z.infer<typeof resetPasswordSchema>;
export type ChangePasswordFormValues = z.infer<typeof changePasswordSchema>;
