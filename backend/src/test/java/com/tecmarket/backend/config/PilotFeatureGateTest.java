package com.tecmarket.backend.config;

import com.tecmarket.backend.repairorder.controller.CommissionSettlementController;
import com.tecmarket.backend.repairorder.controller.RepairOrderCommissionController;
import com.tecmarket.backend.review.controller.PublicMarketplaceController;
import com.tecmarket.backend.sale.controller.SaleController;
import com.tecmarket.backend.subscription.controller.BusinessSubscriptionController;
import com.tecmarket.backend.subscription.controller.SubscriptionPlanController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PilotFeatureGateTest {

    @Test
    void nonEssentialPilotControllersRequireAnExplicitFeatureFlag() {
        Map<Class<?>, String> expectedFlags = Map.of(
                PublicMarketplaceController.class, "marketplace-enabled",
                SubscriptionPlanController.class, "subscriptions-enabled",
                BusinessSubscriptionController.class, "subscriptions-enabled",
                RepairOrderCommissionController.class, "commissions-enabled",
                CommissionSettlementController.class, "commissions-enabled",
                SaleController.class, "direct-sales-enabled");

        expectedFlags.forEach((controller, expectedFlag) -> {
            ConditionalOnProperty condition = AnnotatedElementUtils.findMergedAnnotation(
                    controller, ConditionalOnProperty.class);
            assertThat(condition).as(controller.getSimpleName()).isNotNull();
            assertThat(condition.prefix()).isEqualTo("app.features");
            assertThat(condition.name()).containsExactly(expectedFlag);
            assertThat(condition.havingValue()).isEqualTo("true");
            assertThat(condition.matchIfMissing()).isFalse();
        });
    }
}
