package com.tecmarket.backend.subscription.service;

import com.tecmarket.backend.subscription.entity.SubscriptionPlan;
import com.tecmarket.backend.subscription.repository.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class SubscriptionSeedService implements ApplicationRunner {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Value("${app.seed.enabled:false}")
    private boolean enabled;

    public SubscriptionSeedService(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }

        upsertPlan(
                "BASIC",
                "Basico",
                "59000.00",
                "590000.00",
                2,
                1,
                1,
                "[\"Ordenes de reparacion\", \"Citas\", \"Marketplace publico\", \"Checklist de recepcion\"]");
        upsertPlan(
                "PRO",
                "Pro",
                "99000.00",
                "999000.00",
                4,
                1,
                2,
                "[\"Ordenes de reparacion\", \"Citas\", \"Cotizaciones\", \"Inventario\", \"Ventas\", \"Reportes basicos\"]");
        upsertPlan(
                "PREMIUM",
                "Premium",
                "179000.00",
                "1790000.00",
                8,
                1,
                5,
                "[\"Ordenes de reparacion\", \"Citas\", \"Cotizaciones\", \"Inventario\", \"Ventas\", \"Reportes avanzados\", \"Soporte prioritario\"]");
    }

    private void upsertPlan(
            String code,
            String name,
            String monthlyPrice,
            String yearlyPrice,
            Integer includedUsers,
            Integer maxBusinesses,
            Integer maxBranches,
            String features) {
        SubscriptionPlan plan = subscriptionPlanRepository.findByCodeAndDeletedAtIsNull(code)
                .orElseGet(SubscriptionPlan::new);
        plan.setCode(code);
        plan.setName(name);
        plan.setMonthlyPrice(new BigDecimal(monthlyPrice));
        plan.setYearlyPrice(new BigDecimal(yearlyPrice));
        plan.setIncludedUsers(includedUsers);
        plan.setMaxBusinesses(maxBusinesses);
        plan.setMaxBranches(maxBranches);
        plan.setFeatures(features);
        plan.setActive(true);
        plan.setDeletedAt(null);
        subscriptionPlanRepository.save(plan);
    }
}
