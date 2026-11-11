import { mkdir } from "node:fs/promises";
import path from "node:path";

import { expect, test, type Page, type Route } from "@playwright/test";

import type { RepairOrder } from "@/types/repair-order";

const reviewDir = path.resolve(".impeccable/review");

const user = { id: 7, documentTypeCode: "CC", documentNumber: "1075000007", name: "Laura", surname: "Méndez", email: "laura@example.com", phone: "+573001112233", countryId: 1, countryName: "Colombia", departmentId: 2, departmentName: "Huila", cityId: 3, cityName: "Neiva" };
const business = { id: 10, documentTypeCode: "NIT", documentNumber: "901234567-1", name: "Tecno Centro", legalName: "Tecno Centro S.A.S.", email: "contacto@tecnocentro.com", phone: "+573001119999", address: "Calle 10 # 2-30", description: null, cityId: 3, cityName: "Neiva", logo: null, coverImage: null, ratingAverage: 0, ratingCount: 0, active: true };
const branches = [
  { id: 101, branchNumber: 1, name: "Sede principal", cityId: 3, cityName: "Neiva", address: "Calle 10 # 2-30", primaryBranch: true },
  { id: 102, branchNumber: 2, name: "Sede norte", cityId: 3, cityName: "Neiva", address: "Carrera 5 # 18-20", primaryBranch: false },
];
const statuses = [
  { id: 1, businessId: 10, catalogType: "REPAIR_ORDER_STATUS", code: "RECEIVED", name: "Recibida", description: null, icon: null, sortOrder: 10, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
  { id: 2, businessId: 10, catalogType: "REPAIR_ORDER_STATUS", code: "DIAGNOSIS", name: "En diagnóstico", description: null, icon: null, sortOrder: 20, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
  { id: 3, businessId: 10, catalogType: "REPAIR_ORDER_STATUS", code: "WAITING_APPROVAL", name: "Esperando aprobación", description: null, icon: null, sortOrder: 30, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
];
const devices = [{ id: 301, businessId: 10, customerId: 21, customerDocumentTypeCode: "CC", customerDocumentNumber: "55157801", customerName: "María", customerSurname: "Pérez", customerEmail: "maria@example.com", deviceTypeId: 11, deviceTypeCode: "PHONE", deviceTypeName: "Celular", brandId: 12, brandName: "Apple", model: "iPhone 13 Pro", serialNumber: "SN-ABC-12345", imei: "351756051523999", registeredByBusinessUserId: 71, registeredByBusinessUserName: "Laura Méndez", color: "Azul", registeredAt: "2026-09-20T10:00:00", active: true }];
const services = [{ id: 401, businessId: 10, serviceCategoryId: 31, serviceCategoryCode: "REPAIR", serviceCategoryName: "Reparación", deviceTypeId: 11, deviceTypeCode: "PHONE", deviceTypeName: "Celular", name: "Diagnóstico de encendido", description: "Revisión inicial del circuito de alimentación.", basePrice: 60000, estimatedTimeMinutes: 60, imageUrl: null, active: true }];
const technicians = [{ businessUserId: 81, userId: 17, userName: "Andrés", userSurname: "Torres", userEmail: "andres@example.com", roleCode: "TECHNICIAN", roleName: "Técnico", commissionPercentage: 12 }];

function repairOrder(overrides: Partial<RepairOrder> = {}): RepairOrder {
  return {
    id: 601,
    orderNumber: "OS-010-01-000001",
    orderSequence: 1,
    businessId: 10,
    branchId: 101,
    branchNumber: 1,
    branchName: "Sede principal",
    customerId: 21,
    customerName: "María",
    customerSurname: "Pérez",
    customerEmail: "maria@example.com",
    customerDeviceId: 301,
    customerDeviceLabel: "Apple iPhone 13 Pro",
    appointmentId: null,
    businessServiceId: 401,
    businessServiceName: "Diagnóstico de encendido",
    statusId: 1,
    statusCode: "RECEIVED",
    statusName: "Recibida",
    receivedByBusinessUserId: 71,
    receivedByBusinessUserName: "Laura Méndez",
    assignedBusinessUserId: 81,
    assignedBusinessUserName: "Andrés Torres",
    problemDescription: "El equipo no enciende después de una caída.",
    diagnosis: null,
    estimatedPrice: 180000,
    finalPrice: null,
    receivedAt: "2026-09-23T10:30:00",
    estimatedDeliveryAt: "2026-09-25T17:00:00",
    deliveredAt: null,
    active: true,
    ...overrides,
  };
}

async function fulfill(route: Route, data: unknown, status = 200, message = "ok") {
  await route.fulfill({ status, contentType: "application/json", body: JSON.stringify({ success: status < 400, message, data }) });
}

function pageData<T>(items: T[], size = 12) {
  return { items, page: 0, size, totalElements: items.length, totalPages: items.length ? 1 : 0, hasNext: false, hasPrevious: false };
}

async function mockRepairOrders(page: Page, options: { canViewQuotations?: boolean; canUpdateQuotations?: boolean; failQuotationOnce?: boolean } = {}) {
  const canViewQuotations = options.canViewQuotations ?? true;
  const canUpdateQuotations = options.canUpdateQuotations ?? false;
  let records = [
    repairOrder(),
    repairOrder({ id: 602, orderNumber: "OS-010-02-000014", branchId: 102, branchNumber: 2, branchName: "Sede norte", customerName: "Carlos", customerSurname: "Rojas", customerEmail: "carlos@example.com", statusId: 2, statusCode: "DIAGNOSIS", statusName: "En diagnóstico", customerDeviceLabel: "Samsung Galaxy S24", estimatedPrice: null, receivedAt: "2026-09-22T14:10:00", problemDescription: "Se reinicia al conectar el cargador.", diagnosis: "Pendiente revisar consumo en fuente." }),
    repairOrder({ id: 603, orderNumber: "OS-010-01-000002", statusId: 3, statusCode: "WAITING_APPROVAL", statusName: "Esperando aprobación", customerName: "Diana", customerSurname: "Castro", customerEmail: "diana@example.com", customerDeviceLabel: "Apple MacBook Air M2", businessServiceName: "Revisión de placa", receivedAt: "2026-09-21T08:15:00" }),
  ];
  let attachments: Array<Record<string, unknown>> = [];
  let uploadContentType = "";
  let quotationRequests = 0;
  let quotationPayload: Record<string, unknown> | null = null;
  let createdQuotation: Record<string, unknown> | null = null;
  let quotationStatus = { statusCode: "PENDING", statusName: "Pendiente" };
  let quotationStatusPayload: Record<string, unknown> | null = null;
  let failQuotationOnce = options.failQuotationOnce ?? false;

  await page.route("https://api-dev.juanodsk.dev/**", async (route) => {
    const url = new URL(route.request().url());
    const apiPath = url.pathname;
    const method = route.request().method();
    if (apiPath === "/api/v1/auth/refresh") return fulfill(route, { accessToken: "owner-token", user });
    if (apiPath === "/api/v1/auth/me") return fulfill(route, user);
    if (apiPath === "/api/v1/auth/me/permissions") return fulfill(route, { userId: 7, roles: ["USER"], permissions: [] });
    if (apiPath === "/api/v1/businesses/my") return fulfill(route, [business]);
    if (apiPath === "/api/v1/businesses/10/me/context") return fulfill(route, { userId: 7, businessId: 10, businessName: "Tecno Centro", businessLogo: null, platformAdmin: false, businessUserId: 71, roleId: 2, roleCode: "OWNER", roleName: "Propietario", permissions: ["REPAIR_ORDER_VIEW", "REPAIR_ORDER_CREATE", "REPAIR_ORDER_UPLOAD_EVIDENCE", "QUOTATION_CREATE", ...(canViewQuotations ? ["QUOTATION_VIEW"] : []), ...(canUpdateQuotations ? ["QUOTATION_UPDATE"] : []), "CUSTOMER_DEVICE_VIEW", "BUSINESS_SERVICE_VIEW", "BUSINESS_CATALOG_VIEW"], branches });
    if (apiPath === "/api/v1/businesses/10/customer-devices") return fulfill(route, pageData(devices));
    if (apiPath === "/api/v1/businesses/10/services") return fulfill(route, pageData(services));
    if (apiPath === "/api/v1/businesses/10/catalogs/REPAIR_ORDER_STATUS") return fulfill(route, statuses);
    if (apiPath === "/api/v1/businesses/10/catalogs/QUOTATION_STATUS") return fulfill(route, [
      { id: 21, businessId: 10, catalogType: "QUOTATION_STATUS", code: "DRAFT", name: "Borrador", description: null, icon: null, sortOrder: 10, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
      { id: 22, businessId: 10, catalogType: "QUOTATION_STATUS", code: "PENDING", name: "Pendiente", description: null, icon: null, sortOrder: 20, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
      { id: 23, businessId: 10, catalogType: "QUOTATION_STATUS", code: "APPROVED", name: "Aprobada", description: null, icon: null, sortOrder: 30, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
      { id: 24, businessId: 10, catalogType: "QUOTATION_STATUS", code: "REJECTED", name: "Rechazada", description: null, icon: null, sortOrder: 40, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
      { id: 25, businessId: 10, catalogType: "QUOTATION_STATUS", code: "EXPIRED", name: "Vencida", description: null, icon: null, sortOrder: 50, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
      { id: 26, businessId: 10, catalogType: "QUOTATION_STATUS", code: "CANCELLED", name: "Cancelada", description: null, icon: null, sortOrder: 60, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
    ]);
    if (apiPath === "/api/v1/businesses/10/users/technicians") return fulfill(route, technicians);
    if (apiPath === "/api/v1/businesses/10/repair-orders" && method === "GET") return fulfill(route, pageData(records));
    if (apiPath === "/api/v1/businesses/10/repair-orders/601/history" && method === "GET") {
      return fulfill(route, pageData([
        { id: 801, repairOrderId: 601, statusId: 1, statusCode: "RECEIVED", statusName: "Recibida", createdByUserId: 7, createdByUserName: "Laura Méndez", createdByUserEmail: "laura@example.com", comment: "Orden creada en estado Recibida.", visibility: "INTERNAL", createdAt: "2026-09-23T10:30:00" },
      ], 20));
    }
    if (apiPath === "/api/v1/businesses/10/repair-orders/601/quotations" && method === "GET") {
      quotationRequests += 1;
      const page = Number(url.searchParams.get("page") ?? 0);
      const original = { id: 701, businessId: 10, repairOrderId: 601, customerId: 21, customerName: "María Pérez", ...quotationStatus, totalAmount: 230000, notes: "Cotización válida por 5 días.", expiresAt: "2026-09-28T17:00:00", createdAt: "2026-09-23T11:00:00", items: [
          { id: 711, description: "Cambio de pantalla", quantity: 1, unitPrice: 200000, totalPrice: 200000 },
          { id: 712, description: "Mano de obra", quantity: 1, unitPrice: 30000, totalPrice: 30000 },
        ] };
      const older = { id: 702, businessId: 10, repairOrderId: 601, customerId: 21, customerName: "María Pérez", statusCode: "PENDING", statusName: "Pendiente", totalAmount: 50000, notes: null, expiresAt: null, createdAt: "2026-09-22T11:00:00", items: [
          { id: 713, description: "Diagnóstico avanzado", quantity: 1, unitPrice: 50000, totalPrice: 50000 },
        ] };
      const items = page === 0 ? [createdQuotation, original].filter(Boolean) : [older];
      return fulfill(route, { items, page, size: 20, totalElements: createdQuotation ? 3 : 2, totalPages: 2, hasNext: page === 0, hasPrevious: page === 1 });
    }
    if (apiPath === "/api/v1/businesses/10/quotations" && method === "POST") {
      quotationPayload = route.request().postDataJSON() as Record<string, unknown>;
      if (failQuotationOnce) {
        failQuotationOnce = false;
        return fulfill(route, null, 409, "La operación entra en conflicto con el estado actual de los datos.");
      }
      const bodyItems = quotationPayload.items as Array<{ description: string; quantity: number; unitPrice: number }>;
      createdQuotation = {
        id: 703, businessId: 10, repairOrderId: Number(quotationPayload.repairOrderId), customerId: 21, customerName: "María Pérez",
        statusCode: "DRAFT", statusName: "Borrador", totalAmount: bodyItems.reduce((sum, item) => sum + item.quantity * item.unitPrice, 0),
        notes: typeof quotationPayload.notes === "string" ? quotationPayload.notes : null,
        expiresAt: typeof quotationPayload.expiresAt === "string" ? quotationPayload.expiresAt : null,
        createdAt: "2026-09-25T11:00:00",
        items: bodyItems.map((item, index) => ({ id: 720 + index, ...item, totalPrice: item.quantity * item.unitPrice })),
      };
      return fulfill(route, createdQuotation, 201, "Cotización creada.");
    }
    if (apiPath === "/api/v1/businesses/10/quotations/701/status" && method === "PATCH") {
      quotationStatusPayload = route.request().postDataJSON() as Record<string, unknown>;
      quotationStatus = { statusCode: "APPROVED", statusName: "Aprobada" };
      return fulfill(route, { id: 701, businessId: 10, repairOrderId: 601, customerId: 21, customerName: "María Pérez", ...quotationStatus, totalAmount: 230000, notes: "Aprobada por el cliente.", expiresAt: "2026-09-28T17:00:00", createdAt: "2026-09-23T11:00:00", items: [{ id: 711, description: "Cambio de pantalla", quantity: 1, unitPrice: 200000, totalPrice: 200000 }, { id: 712, description: "Mano de obra", quantity: 1, unitPrice: 30000, totalPrice: 30000 }] });
    }
    if (apiPath === "/api/v1/businesses/10/repair-orders/601/attachments" && method === "GET") return fulfill(route, pageData(attachments, 20));
    if (apiPath === "/api/v1/businesses/10/repair-orders/601/attachments/upload" && method === "POST") {
      uploadContentType = route.request().headers()["content-type"] ?? "";
      const created = { id: 910, repairOrderId: 601, uploadedByBusinessUserId: 71, uploadedByEmail: "laura@example.com", uploadedByName: "Laura Méndez", fileUrl: "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 4 3'%3E%3Crect width='4' height='3' fill='%232458d8'/%3E%3C/svg%3E", originalFileName: "antes-reparacion.jpg", contentType: "image/png", fileSize: 524288, checksumSha256: "f6b0c1d2e3f4567890abcdef1234567890abcdef1234567890abcdef12345678", description: "Vista frontal antes del trabajo.", createdAt: "2026-09-23T10:35:00" };
      attachments = [created, ...attachments];
      return fulfill(route, created, 201, "Evidencia cargada.");
    }
    const deleteAttachmentPath = apiPath.match(/^\/api\/v1\/businesses\/10\/repair-orders\/601\/attachments\/(\d+)$/);
    if (deleteAttachmentPath && method === "DELETE") {
      attachments = attachments.filter((attachment) => attachment.id !== Number(deleteAttachmentPath[1]));
      return fulfill(route, null, 200, "Evidencia eliminada.");
    }
    if (apiPath === "/api/v1/businesses/10/repair-orders" && method === "POST") {
      const body = route.request().postDataJSON() as Record<string, unknown>;
      const created = repairOrder({ id: 604, orderNumber: "OS-010-01-000004", problemDescription: String(body.problemDescription), estimatedPrice: Number(body.estimatedPrice), receivedAt: String(body.receivedAt ?? "2026-09-23T23:55:00") });
      records = [created, ...records];
      return fulfill(route, created, 201, "Orden creada.");
    }
    return fulfill(route, null, 404, "No encontrado");
  });

  return {
    getUploadContentType: () => uploadContentType,
    getQuotationRequests: () => quotationRequests,
    getQuotationPayload: () => quotationPayload,
    getQuotationStatusPayload: () => quotationStatusPayload,
  };
}

test("muestra el registro y abre el detalle", async ({ page }) => {
  await mkdir(reviewDir, { recursive: true });
  await page.setViewportSize({ width: 1440, height: 1000 });
  await mockRepairOrders(page);
  await page.goto("/app/negocios/10/ordenes");
  await expect(page.getByRole("heading", { name: "Órdenes de reparación", exact: true })).toBeVisible();
  await expect(page.getByText("OS-010-01-000001").first()).toBeVisible();
  await page.screenshot({ path: path.join(reviewDir, "repair-orders-desktop.png"), fullPage: true });
  await page.getByRole("button", { name: "Ver detalle" }).first().click();
  await expect(page.getByRole("heading", { name: "Orden OS-010-01-000001" })).toBeVisible();
  await expect(page.getByText("El equipo no enciende después de una caída.")).toBeVisible();
  await expect(page.getByText("Aún no hay evidencias asociadas a esta orden.")).toBeVisible();
  await expect(page.getByText("Orden creada en estado Recibida.")).toBeVisible();
  await expect(page.getByRole("heading", { name: "Cotizaciones" })).toBeVisible();
  await expect(page.getByText("Cambio de pantalla")).toBeVisible();
  await expect(page.getByText("Cotización válida por 5 días.")).toBeVisible();
  await expect(page.getByText("Página 1 de 2")).toBeVisible();
  await page.getByRole("button", { name: "Cotizaciones anteriores" }).click();
  await expect(page.getByText("Diagnóstico avanzado")).toBeVisible();
  await expect(page.getByText("Página 2 de 2")).toBeVisible();
  await page.screenshot({ path: path.join(reviewDir, "repair-order-detail-desktop.png") });
  await page.setViewportSize({ width: 390, height: 844 });
  await page.screenshot({ path: path.join(reviewDir, "repair-order-detail-mobile.png") });
  await page.locator("#repair-order-evidence-title").scrollIntoViewIfNeeded();
  await page.screenshot({ path: path.join(reviewDir, "repair-order-evidence-mobile.png") });
});

test("crea cotización con QUOTATION_CREATE sin consultar la lista si falta QUOTATION_VIEW", async ({ page }) => {
  const mocks = await mockRepairOrders(page, { canViewQuotations: false });
  await page.goto("/app/negocios/10/ordenes");
  await page.getByRole("button", { name: "Ver detalle" }).first().click();

  await page.getByRole("button", { name: "Nueva cotización" }).click();
  const form = page.locator(".tm-quotation-form");
  await form.getByLabel("Descripción", { exact: true }).fill("Cambio de módulo de pantalla");
  await form.getByLabel("Precio unitario", { exact: true }).fill("200000");
  await form.getByLabel("Notas (opcional)").fill("Incluye prueba de funcionamiento.");
  await page.getByRole("button", { name: "Crear cotización" }).click();

  await expect(page.getByRole("status").filter({ hasText: "quedó creada como borrador" })).toBeVisible();
  expect(mocks.getQuotationRequests()).toBe(0);
  expect(mocks.getQuotationPayload()).toEqual({
    repairOrderId: 601,
    notes: "Incluye prueba de funcionamiento.",
    items: [{ description: "Cambio de módulo de pantalla", quantity: 1, unitPrice: 200000 }],
  });
});

test("crea una cotización desde el detalle y la muestra en la lista", async ({ page }) => {
  await mkdir(reviewDir, { recursive: true });
  const mocks = await mockRepairOrders(page, { failQuotationOnce: true });
  await page.setViewportSize({ width: 1440, height: 1000 });
  await page.goto("/app/negocios/10/ordenes");
  await page.getByRole("button", { name: "Ver detalle" }).first().click();
  await page.getByRole("button", { name: "Nueva cotización" }).click();
  const form = page.locator(".tm-quotation-form");

  await form.getByLabel("Descripción", { exact: true }).fill("Cambio de módulo de pantalla");
  await form.getByLabel("Cantidad", { exact: true }).fill("1");
  await form.getByLabel("Precio unitario", { exact: true }).fill("200000");
  await form.getByLabel("Notas (opcional)").fill("Incluye prueba de funcionamiento.");
  await form.getByLabel("Vence el (opcional)").fill("2026-09-30T17:00");
  await expect(page.locator(".tm-quotation-form__heading")).toContainText("200.000");
  await page.screenshot({ path: path.join(reviewDir, "quotation-editor-desktop.png"), fullPage: true });
  await page.setViewportSize({ width: 390, height: 844 });
  await page.screenshot({ path: path.join(reviewDir, "quotation-editor-mobile.png"), fullPage: true });
  await page.getByRole("button", { name: "Crear cotización" }).click();
  await expect(form.getByRole("alert")).toContainText("conflicto con el estado actual");
  await expect(form.getByLabel("Descripción", { exact: true })).toHaveValue("Cambio de módulo de pantalla");
  await page.getByRole("button", { name: "Crear cotización" }).click();

  await expect(page.getByRole("status").filter({ hasText: "quedó creada como borrador" })).toBeVisible();
  await expect(page.getByText("Cambio de módulo de pantalla")).toBeVisible();
  expect(mocks.getQuotationPayload()).toEqual({
    repairOrderId: 601,
    notes: "Incluye prueba de funcionamiento.",
    expiresAt: "2026-09-30T17:00",
    items: [{ description: "Cambio de módulo de pantalla", quantity: 1, unitPrice: 200000 }],
  });
});

test("actualiza el estado de una cotización con QUOTATION_UPDATE usando el catálogo del negocio", async ({ page }) => {
  await mkdir(reviewDir, { recursive: true });
  const mocks = await mockRepairOrders(page, { canUpdateQuotations: true });
  await page.setViewportSize({ width: 1440, height: 1000 });
  await page.goto("/app/negocios/10/ordenes");
  await page.getByRole("button", { name: "Ver detalle" }).first().click();

  const quotation = page.locator(".tm-repair-quotations__item").filter({ hasText: "Cambio de pantalla" }).first();
  await quotation.getByRole("button", { name: "Cambiar estado" }).click();
  await expect(quotation.getByLabel("Nuevo estado")).toHaveValue("23");
  await expect(quotation.getByLabel("Nuevo estado")).toContainText("Aprobada");
  await page.screenshot({ path: path.join(reviewDir, "quotation-status-desktop.png"), fullPage: true });
  await page.setViewportSize({ width: 390, height: 844 });
  await page.screenshot({ path: path.join(reviewDir, "quotation-status-mobile.png"), fullPage: true });
  await quotation.getByLabel("Notas de la cotización (opcional; vacío conserva las actuales)").fill("Aprobada por el cliente.");
  await quotation.getByRole("button", { name: "Guardar estado" }).click();

  await expect(page.getByRole("status").filter({ hasText: "cambió a Aprobada" })).toBeVisible();
  await expect(quotation.getByText("Aprobada", { exact: true })).toBeVisible();
  expect(mocks.getQuotationStatusPayload()).toEqual({ statusId: 23, notes: "Aprobada por el cliente." });
});

test("carga una evidencia fotográfica y la elimina con confirmación", async ({ page }) => {
  const mocks = await mockRepairOrders(page);
  await page.setViewportSize({ width: 1280, height: 900 });
  await page.goto("/app/negocios/10/ordenes");
  await page.getByRole("button", { name: "Ver detalle" }).first().click();

  await page.locator("#repair-evidence-file").setInputFiles({
    name: "antes-reparacion.jpg",
    mimeType: "image/jpeg",
    buffer: Buffer.from("mock image"),
  });
  await page.locator("#repair-evidence-description").fill("Vista frontal antes del trabajo.");
  await page.getByRole("button", { name: "Cargar evidencia" }).click();
  expect(mocks.getUploadContentType()).toContain("multipart/form-data");
  const evidenceCard = page.locator(".tm-repair-evidence__item");
  await expect(evidenceCard.getByText("antes-reparacion.jpg")).toBeVisible();
  await expect(evidenceCard.getByText("Vista frontal antes del trabajo.")).toBeVisible();

  await evidenceCard.getByRole("button", { name: "Eliminar" }).click();
  await expect(evidenceCard.getByText("¿Eliminar esta evidencia?")).toBeVisible();
  await evidenceCard.getByRole("button", { name: "Confirmar" }).click();
  await expect(page.getByText("Aún no hay evidencias asociadas a esta orden.")).toBeVisible();
});

test("crea una orden en móvil y tema oscuro", async ({ page }) => {
  await mkdir(reviewDir, { recursive: true });
  await page.setViewportSize({ width: 360, height: 800 });
  await mockRepairOrders(page);
  await page.goto("/app/negocios/10/ordenes");
  await page.getByRole("button", { name: "Usar tema oscuro" }).click();
  await page.screenshot({ path: path.join(reviewDir, "repair-orders-mobile-dark.png"), fullPage: true });
  await page.getByRole("button", { name: "Recibir equipo" }).click();
  await expect(page.getByRole("heading", { name: "Recibir equipo" })).toBeVisible();
  await expect(page.getByLabel("Sede", { exact: true })).toHaveValue("101");
  await page.getByLabel("Técnico asignado").selectOption("81");
  const editor = page.locator(".tm-repair-form");
  await editor.getByLabel("Cliente", { exact: true }).selectOption("21");
  await editor.getByLabel("Dispositivo", { exact: true }).selectOption("301");
  await editor.getByLabel("Servicio", { exact: true }).selectOption("401");
  await page.getByLabel("Problema reportado").fill("No carga y se apaga al retirar el cable.");
  await page.getByLabel("Valor estimado").fill("150000");
  await page.locator(".tm-review-panel__body").evaluate((element) => { element.scrollTop = 0; });
  await page.screenshot({ path: path.join(reviewDir, "repair-order-editor-mobile-dark.png") });
  await editor.getByRole("button", { name: "Crear orden" }).click();
  await expect(page.getByText("La orden OS-010-01-000004 quedó creada en estado Recibida.")).toBeVisible();
});
