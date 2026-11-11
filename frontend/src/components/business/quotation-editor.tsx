"use client";

import { LoaderCircle, Plus, Trash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useRef, useState, type FormEvent } from "react";

import { FormAlert } from "@/components/ui/feedback";
import { createRepairOrderQuotation } from "@/features/repair-orders/api";
import { quotationDraftSchema, type QuotationDraftValues } from "@/features/quotations/schemas";
import { normalizeApiError } from "@/lib/api/errors";
import type { Quotation } from "@/types/quotation";

interface QuotationEditorProps {
  businessId: number;
  repairOrderId: number;
  onCancel: () => void;
  onCreated: (quotation: Quotation) => void;
}

type DraftItem = QuotationDraftValues["items"][number] & { key: number };

function formatMoney(value: number) {
  return new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", maximumFractionDigits: 0 }).format(value);
}

export function QuotationEditor({ businessId, repairOrderId, onCancel, onCreated }: QuotationEditorProps) {
  const router = useRouter();
  const nextKey = useRef(2);
  const [items, setItems] = useState<DraftItem[]>([{ key: 1, description: "", quantity: "1", unitPrice: "" }]);
  const [notes, setNotes] = useState("");
  const [expiresAt, setExpiresAt] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const total = items.reduce((sum, item) => {
    const quantity = Number(item.quantity);
    const price = Number(item.unitPrice.replace(",", "."));
    return Number.isFinite(quantity) && Number.isFinite(price) ? sum + quantity * price : sum;
  }, 0);

  function updateItem(key: number, field: "description" | "quantity" | "unitPrice", value: string) {
    setItems((current) => current.map((item) => item.key === key ? { ...item, [field]: value } : item));
    setError(null);
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);
    const parsed = quotationDraftSchema.safeParse({ notes, expiresAt, items: items.map(({ description, quantity, unitPrice }) => ({ description, quantity, unitPrice })) });
    if (!parsed.success) {
      setError(parsed.error.issues[0]?.message ?? "Revisa los datos de la cotización.");
      return;
    }

    const values = parsed.data;
    setSubmitting(true);
    try {
      const quotation = await createRepairOrderQuotation(businessId, {
        repairOrderId,
        ...(values.notes.trim() ? { notes: values.notes.trim() } : {}),
        ...(values.expiresAt ? { expiresAt: values.expiresAt } : {}),
        items: values.items.map((item) => ({
          description: item.description.trim(),
          quantity: Number(item.quantity),
          unitPrice: Number(item.unitPrice.replace(",", ".")),
        })),
      });
      onCreated(quotation);
    } catch (cause) {
      const normalized = normalizeApiError(cause);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setError(normalized.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form className="tm-quotation-form" onSubmit={handleSubmit} noValidate>
      <header className="tm-quotation-form__heading">
        <div>
          <h4>Nueva cotización</h4>
          <p>Agrega los trabajos y repuestos que propones para esta orden.</p>
        </div>
        <strong aria-live="polite">Total provisional <span>{formatMoney(total)}</span></strong>
      </header>

      {error ? <FormAlert>{error}</FormAlert> : null}

      <div className="tm-quotation-form__items">
        {items.map((item, index) => (
          <fieldset className="tm-quotation-form__item" key={item.key}>
            <legend>Concepto {index + 1}</legend>
            <label className="tm-field tm-quotation-form__description" htmlFor={`quotation-description-${item.key}`}>
              <span>Descripción</span>
              <input id={`quotation-description-${item.key}`} value={item.description} maxLength={300} required onChange={(event) => updateItem(item.key, "description", event.target.value)} placeholder="Ej. Cambio de pantalla" />
            </label>
            <label className="tm-field" htmlFor={`quotation-quantity-${item.key}`}>
              <span>Cantidad</span>
              <input id={`quotation-quantity-${item.key}`} type="number" min="1" step="1" value={item.quantity} required onChange={(event) => updateItem(item.key, "quantity", event.target.value)} />
            </label>
            <label className="tm-field" htmlFor={`quotation-price-${item.key}`}>
              <span>Precio unitario</span>
              <input id={`quotation-price-${item.key}`} type="number" min="0.01" step="0.01" value={item.unitPrice} required onChange={(event) => updateItem(item.key, "unitPrice", event.target.value)} placeholder="0" />
            </label>
            {items.length > 1 ? <button className="tm-icon-button tm-quotation-form__remove" type="button" aria-label={`Quitar concepto ${index + 1}`} onClick={() => setItems((current) => current.filter((candidate) => candidate.key !== item.key))}><Trash2 aria-hidden="true" /></button> : null}
          </fieldset>
        ))}
      </div>

      <button className="tm-button tm-button--secondary tm-quotation-form__add" type="button" disabled={items.length >= 100} onClick={() => setItems((current) => [...current, { key: nextKey.current++, description: "", quantity: "1", unitPrice: "" }])}>
        <Plus aria-hidden="true" /> Agregar concepto
      </button>

      <div className="tm-quotation-form__optional">
        <label className="tm-textarea-field" htmlFor="quotation-notes">
          <span>Notas <small>(opcional)</small></span>
          <textarea id="quotation-notes" value={notes} maxLength={500} rows={3} onChange={(event) => { setNotes(event.target.value); setError(null); }} placeholder="Información que deba acompañar la cotización" />
          <small>{notes.length}/500</small>
        </label>
        <label className="tm-field" htmlFor="quotation-expires-at">
          <span>Vence el <small>(opcional)</small></span>
          <input id="quotation-expires-at" type="datetime-local" value={expiresAt} onChange={(event) => { setExpiresAt(event.target.value); setError(null); }} />
        </label>
      </div>

      <footer className="tm-repair-form__actions tm-quotation-form__actions">
        <button className="tm-button tm-button--secondary" type="button" onClick={onCancel} disabled={submitting}>Cancelar</button>
        <button className="tm-button tm-button--primary" type="submit" disabled={submitting}>
          {submitting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Plus aria-hidden="true" />}
          {submitting ? "Creando cotización…" : "Crear cotización"}
        </button>
      </footer>
    </form>
  );
}
