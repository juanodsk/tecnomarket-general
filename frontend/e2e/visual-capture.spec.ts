import { mkdir } from "node:fs/promises";
import path from "node:path";

import { expect, test } from "@playwright/test";

const reviewDir = path.resolve(".impeccable/review");

test("captura evidencia visual de las superficies públicas", async ({ page }) => {
  await mkdir(reviewDir, { recursive: true });
  await page.route("https://api-dev.juanodsk.dev/**", (route) =>
    route.fulfill({
      status: 400,
      contentType: "application/json",
      body: JSON.stringify({ success: false, message: "Sin sesión", data: null }),
    }),
  );

  await page.setViewportSize({ width: 1440, height: 1000 });
  await page.goto("/");
  await expect(page.getByRole("heading", { name: "Cada reparación necesita una historia verificable." })).toBeVisible();
  await page.screenshot({ path: path.join(reviewDir, "desktop.png"), fullPage: true });

  await page.setViewportSize({ width: 360, height: 800 });
  await page.reload();
  await expect(page.getByRole("heading", { name: "Cada reparación necesita una historia verificable." })).toBeVisible();
  await page.screenshot({ path: path.join(reviewDir, "mobile.png"), fullPage: true });

  await page.setViewportSize({ width: 1440, height: 1000 });
  await page.goto("/login");
  await expect(page.getByRole("heading", { name: "Continúa tu operación" })).toBeVisible();
  await page.screenshot({ path: path.join(reviewDir, "auth-desktop.png"), fullPage: true });

  await page.setViewportSize({ width: 360, height: 800 });
  await page.reload();
  await page.getByRole("button", { name: "Usar tema oscuro" }).click();
  await page.screenshot({ path: path.join(reviewDir, "auth-mobile-dark.png"), fullPage: true });
});
