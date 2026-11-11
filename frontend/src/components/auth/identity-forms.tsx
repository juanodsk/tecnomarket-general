"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@heroui/react";
import { ArrowRight, LoaderCircle } from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";

import { FormAlert } from "@/components/ui/feedback";
import { PasswordField, SelectField, TextField } from "@/components/ui/form-controls";
import * as authApi from "@/features/auth/api";
import {
  forgotPasswordSchema,
  loginSchema,
  registerSchema,
  resetPasswordSchema,
  type ForgotPasswordFormValues,
  type LoginFormValues,
  type RegisterFormValues,
  type ResetPasswordFormValues,
} from "@/features/auth/schemas";
import * as locationApi from "@/features/locations/api";
import { apiErrorMessage, normalizeApiError } from "@/lib/api/errors";
import { useAuth } from "@/providers/auth-provider";
import type { City, Country, Department, DocumentType } from "@/types/location";

function SubmitButton({ pending, children }: { pending: boolean; children: React.ReactNode }) {
  return (
    <Button className="tm-button tm-button--primary tm-button--full" type="submit" isDisabled={pending}>
      {pending ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : null}
      {children}
      {!pending ? <ArrowRight aria-hidden="true" /> : null}
    </Button>
  );
}

export function LoginForm({ next = "/app" }: { next?: string }) {
  const router = useRouter();
  const { signIn } = useAuth();
  const [apiError, setApiError] = useState<string | null>(null);
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormValues>({ resolver: zodResolver(loginSchema) });

  const onSubmit = handleSubmit(async (values) => {
    setApiError(null);
    try {
      await signIn(values);
      router.replace(next.startsWith("/") && !next.startsWith("//") ? next : "/app");
    } catch (error) {
      setApiError(apiErrorMessage(error));
    }
  });

  return (
    <form className="tm-form" onSubmit={onSubmit} noValidate>
      {apiError ? <FormAlert>{apiError}</FormAlert> : null}
      <TextField
        label="Correo electrónico"
        type="email"
        autoComplete="email"
        placeholder="nombre@correo.com"
        error={errors.email?.message}
        {...register("email")}
      />
      <PasswordField
        label="Contraseña"
        autoComplete="current-password"
        error={errors.password?.message}
        {...register("password")}
      />
      <div className="tm-form__meta">
        <Link href="/recuperar-contrasena">¿Olvidaste tu contraseña?</Link>
      </div>
      <SubmitButton pending={isSubmitting}>Ingresar</SubmitButton>
      <p className="tm-form__footer">
        ¿Aún no tienes cuenta? <Link href="/registro">Crear cuenta</Link>
      </p>
    </form>
  );
}

export function RegisterForm() {
  const router = useRouter();
  const [apiError, setApiError] = useState<string | null>(null);
  const [catalogError, setCatalogError] = useState<string | null>(null);
  const [documentTypes, setDocumentTypes] = useState<DocumentType[]>([]);
  const [countries, setCountries] = useState<Country[]>([]);
  const [departments, setDepartments] = useState<Department[]>([]);
  const [cities, setCities] = useState<City[]>([]);
  const [loadingLocation, setLoadingLocation] = useState(false);
  const {
    register,
    handleSubmit,
    watch,
    resetField,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormValues>({ resolver: zodResolver(registerSchema) });
  const countryId = watch("countryId");
  const departmentId = watch("departmentId");

  useEffect(() => {
    Promise.all([locationApi.getDocumentTypes(), locationApi.getCountries()])
      .then(([types, countryList]) => {
        setDocumentTypes(types.filter((item) => item.active && item.appliesToPerson));
        setCountries(countryList.filter((item) => item.active));
      })
      .catch((error) => setCatalogError(apiErrorMessage(error)));
  }, []);

  useEffect(() => {
    setDepartments([]);
    setCities([]);
    resetField("departmentId");
    resetField("cityId");
    if (!countryId) return;

    setLoadingLocation(true);
    locationApi
      .getDepartments(Number(countryId))
      .then((items) => setDepartments(items.filter((item) => item.active)))
      .catch((error) => setCatalogError(apiErrorMessage(error)))
      .finally(() => setLoadingLocation(false));
  }, [countryId, resetField]);

  useEffect(() => {
    setCities([]);
    resetField("cityId");
    if (!departmentId) return;

    setLoadingLocation(true);
    locationApi
      .getCities(Number(departmentId))
      .then((items) => setCities(items.filter((item) => item.active)))
      .catch((error) => setCatalogError(apiErrorMessage(error)))
      .finally(() => setLoadingLocation(false));
  }, [departmentId, resetField]);

  const onSubmit = handleSubmit(async (values) => {
    setApiError(null);
    try {
      await authApi.registerAccount({
        documentTypeCode: values.documentTypeCode,
        documentNumber: values.documentNumber,
        name: values.name,
        surname: values.surname,
        email: values.email,
        password: values.password,
        address: values.address,
        phone: values.phone,
        countryId: Number(values.countryId),
        departmentId: Number(values.departmentId),
        cityId: Number(values.cityId),
      });
      router.push(`/login?registro=exitoso&email=${encodeURIComponent(values.email)}`);
    } catch (error) {
      setApiError(apiErrorMessage(error));
    }
  });

  return (
    <form className="tm-form tm-form--wide" onSubmit={onSubmit} noValidate>
      {apiError ? <FormAlert>{apiError}</FormAlert> : null}
      {catalogError ? <FormAlert tone="warning">{catalogError}</FormAlert> : null}
      <fieldset className="tm-form-section">
        <legend>Identificación</legend>
        <div className="tm-form-grid">
          <SelectField label="Tipo de documento" error={errors.documentTypeCode?.message} {...register("documentTypeCode")}>
            <option value="">Selecciona una opción</option>
            {documentTypes.map((item) => (
              <option key={item.id} value={item.code}>
                {item.name}
              </option>
            ))}
          </SelectField>
          <TextField label="Número de documento" error={errors.documentNumber?.message} {...register("documentNumber")} />
          <TextField label="Nombres" autoComplete="given-name" error={errors.name?.message} {...register("name")} />
          <TextField label="Apellidos" autoComplete="family-name" error={errors.surname?.message} {...register("surname")} />
        </div>
      </fieldset>
      <fieldset className="tm-form-section">
        <legend>Contacto y ubicación</legend>
        <div className="tm-form-grid">
          <TextField label="Correo electrónico" type="email" autoComplete="email" error={errors.email?.message} {...register("email")} />
          <TextField label="Teléfono" type="tel" autoComplete="tel" error={errors.phone?.message} {...register("phone")} />
          <TextField className="tm-field--span" label="Dirección" autoComplete="street-address" error={errors.address?.message} {...register("address")} />
          <SelectField label="País" error={errors.countryId?.message} {...register("countryId")}>
            <option value="">Selecciona un país</option>
            {countries.map((item) => (
              <option key={item.id} value={item.id}>{item.name}</option>
            ))}
          </SelectField>
          <SelectField label="Departamento" disabled={!countryId || loadingLocation} error={errors.departmentId?.message} {...register("departmentId")}>
            <option value="">Selecciona un departamento</option>
            {departments.map((item) => (
              <option key={item.id} value={item.id}>{item.name}</option>
            ))}
          </SelectField>
          <SelectField label="Ciudad" disabled={!departmentId || loadingLocation} error={errors.cityId?.message} {...register("cityId")}>
            <option value="">Selecciona una ciudad</option>
            {cities.map((item) => (
              <option key={item.id} value={item.id}>{item.name}</option>
            ))}
          </SelectField>
        </div>
      </fieldset>
      <fieldset className="tm-form-section">
        <legend>Seguridad</legend>
        <div className="tm-form-grid">
          <PasswordField label="Contraseña" autoComplete="new-password" hint="Mínimo 8 caracteres." error={errors.password?.message} {...register("password")} />
          <PasswordField label="Confirmar contraseña" autoComplete="new-password" error={errors.confirmPassword?.message} {...register("confirmPassword")} />
        </div>
      </fieldset>
      <SubmitButton pending={isSubmitting}>Crear cuenta</SubmitButton>
      <p className="tm-form__footer">¿Ya tienes cuenta? <Link href="/login">Ingresar</Link></p>
    </form>
  );
}

export function ForgotPasswordForm() {
  const [feedback, setFeedback] = useState<{ tone: "error" | "success"; message: string } | null>(null);
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<ForgotPasswordFormValues>({ resolver: zodResolver(forgotPasswordSchema) });
  const onSubmit = handleSubmit(async ({ email }) => {
    setFeedback(null);
    try {
      const response = await authApi.requestPasswordReset(email);
      setFeedback({ tone: "success", message: response.message });
    } catch (error) {
      setFeedback({ tone: "error", message: apiErrorMessage(error) });
    }
  });

  return (
    <form className="tm-form" onSubmit={onSubmit} noValidate>
      {feedback ? <FormAlert tone={feedback.tone}>{feedback.message}</FormAlert> : null}
      <TextField label="Correo electrónico" type="email" autoComplete="email" error={errors.email?.message} {...register("email")} />
      <SubmitButton pending={isSubmitting}>Enviar instrucciones</SubmitButton>
      <p className="tm-form__footer"><Link href="/login">Volver al inicio de sesión</Link></p>
    </form>
  );
}

export function ResetPasswordForm({ token }: { token: string }) {
  const [feedback, setFeedback] = useState<{ tone: "error" | "success"; message: string } | null>(null);
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<ResetPasswordFormValues>({
    resolver: zodResolver(resetPasswordSchema),
    defaultValues: { token },
  });
  const onSubmit = handleSubmit(async ({ token: resetToken, newPassword }) => {
    setFeedback(null);
    try {
      const response = await authApi.resetPassword(resetToken, newPassword);
      setFeedback({ tone: "success", message: response.message });
    } catch (error) {
      setFeedback({ tone: "error", message: apiErrorMessage(error) });
    }
  });

  if (!token) return <FormAlert>El enlace no contiene un token de restablecimiento.</FormAlert>;

  return (
    <form className="tm-form" onSubmit={onSubmit} noValidate>
      <input type="hidden" {...register("token")} />
      {feedback ? <FormAlert tone={feedback.tone}>{feedback.message}</FormAlert> : null}
      <PasswordField label="Nueva contraseña" autoComplete="new-password" hint="Mínimo 12 caracteres." error={errors.newPassword?.message} {...register("newPassword")} />
      <PasswordField label="Confirmar contraseña" autoComplete="new-password" error={errors.confirmPassword?.message} {...register("confirmPassword")} />
      <SubmitButton pending={isSubmitting}>Guardar contraseña</SubmitButton>
      {feedback?.tone === "success" ? <p className="tm-form__footer"><Link href="/login">Ingresar con la nueva contraseña</Link></p> : null}
    </form>
  );
}

export function VerifyEmailPanel({ token }: { token: string }) {
  const [state, setState] = useState<{ status: "loading" | "success" | "error"; message: string }>({
    status: "loading",
    message: "Validando el enlace de verificación…",
  });

  useEffect(() => {
    let active = true;
    if (!token) {
      setState({ status: "error", message: "El enlace no contiene un token de verificación." });
      return;
    }
    authApi
      .verifyEmail(token)
      .then((response) => active && setState({ status: "success", message: response.message }))
      .catch((error) => active && setState({ status: "error", message: normalizeApiError(error).message }));
    return () => {
      active = false;
    };
  }, [token]);

  return (
    <div className="tm-form">
      {state.status === "loading" ? (
        <div className="tm-inline-loader" role="status"><LoaderCircle className="tm-spinner" aria-hidden="true" />{state.message}</div>
      ) : (
        <FormAlert tone={state.status}>{state.message}</FormAlert>
      )}
      {state.status !== "loading" ? <Link className="tm-button tm-button--primary tm-button--full" href="/login">Ir al inicio de sesión <ArrowRight aria-hidden="true" /></Link> : null}
    </div>
  );
}
