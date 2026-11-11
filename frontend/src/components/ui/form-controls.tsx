"use client";

import { Eye, EyeOff } from "lucide-react";
import { forwardRef, useId, useState, type InputHTMLAttributes, type SelectHTMLAttributes } from "react";

interface FieldProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
  hint?: string;
}

export const TextField = forwardRef<HTMLInputElement, FieldProps>(function TextField(
  { label, error, hint, id, className, ...props },
  ref,
) {
  const generatedId = useId();
  const fieldId = id ?? generatedId;
  const descriptionId = `${fieldId}-description`;

  return (
    <div className={`tm-field ${className ?? ""}`}>
      <label htmlFor={fieldId}>{label}</label>
      <input
        {...props}
        ref={ref}
        id={fieldId}
        aria-invalid={Boolean(error)}
        aria-describedby={error || hint ? descriptionId : undefined}
      />
      {error ? (
        <p className="tm-field__error" id={descriptionId} role="alert">
          {error}
        </p>
      ) : hint ? (
        <p className="tm-field__hint" id={descriptionId}>
          {hint}
        </p>
      ) : null}
    </div>
  );
});

export const PasswordField = forwardRef<HTMLInputElement, FieldProps>(function PasswordField(
  { label, error, hint, id, className, ...props },
  ref,
) {
  const [visible, setVisible] = useState(false);
  const generatedId = useId();
  const fieldId = id ?? generatedId;
  const descriptionId = `${fieldId}-description`;

  return (
    <div className={`tm-field ${className ?? ""}`}>
      <label htmlFor={fieldId}>{label}</label>
      <div className="tm-password-input">
        <input
          {...props}
          ref={ref}
          id={fieldId}
          type={visible ? "text" : "password"}
          aria-invalid={Boolean(error)}
          aria-describedby={error || hint ? descriptionId : undefined}
        />
        <button
          type="button"
          onClick={() => setVisible((current) => !current)}
          aria-label={visible ? "Ocultar contraseña" : "Mostrar contraseña"}
        >
          {visible ? <EyeOff aria-hidden="true" /> : <Eye aria-hidden="true" />}
        </button>
      </div>
      {error ? (
        <p className="tm-field__error" id={descriptionId} role="alert">
          {error}
        </p>
      ) : hint ? (
        <p className="tm-field__hint" id={descriptionId}>
          {hint}
        </p>
      ) : null}
    </div>
  );
});

interface SelectFieldProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label: string;
  error?: string;
  children: React.ReactNode;
}

export const SelectField = forwardRef<HTMLSelectElement, SelectFieldProps>(function SelectField(
  { label, error, id, children, className, ...props },
  ref,
) {
  const generatedId = useId();
  const fieldId = id ?? generatedId;
  const errorId = `${fieldId}-error`;

  return (
    <div className={`tm-field ${className ?? ""}`}>
      <label htmlFor={fieldId}>{label}</label>
      <select
        {...props}
        ref={ref}
        id={fieldId}
        aria-invalid={Boolean(error)}
        aria-describedby={error ? errorId : undefined}
      >
        {children}
      </select>
      {error ? (
        <p className="tm-field__error" id={errorId} role="alert">
          {error}
        </p>
      ) : null}
    </div>
  );
});
