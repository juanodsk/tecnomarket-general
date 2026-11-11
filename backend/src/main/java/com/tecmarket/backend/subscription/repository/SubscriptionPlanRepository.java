package com.tecmarket.backend.subscription.repository;

import com.tecmarket.backend.subscription.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {

    List<SubscriptionPlan> findAllByDeletedAtIsNullOrderByMonthlyPriceAscNameAsc();

    Optional<SubscriptionPlan> findByIdAndDeletedAtIsNull(Integer id);

    Optional<SubscriptionPlan> findByCodeAndDeletedAtIsNull(String code);

    Optional<SubscriptionPlan> findByCodeAndActiveTrueAndDeletedAtIsNull(String code);

    boolean existsByCodeAndDeletedAtIsNull(String code);

    boolean existsByCodeAndDeletedAtIsNullAndIdNot(String code, Integer id);
}
