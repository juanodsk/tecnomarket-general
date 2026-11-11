package com.tecmarket.backend.subscription.service;

import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.subscription.dto.CreateSubscriptionPlanRequest;
import com.tecmarket.backend.subscription.dto.SubscriptionPlanResponse;
import com.tecmarket.backend.subscription.dto.UpdateSubscriptionPlanRequest;
import com.tecmarket.backend.subscription.entity.SubscriptionPlan;
import com.tecmarket.backend.subscription.repository.SubscriptionPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final BusinessAccessGuard businessAccessGuard;

    public SubscriptionPlanService(
            SubscriptionPlanRepository subscriptionPlanRepository,
            BusinessAccessGuard businessAccessGuard) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.businessAccessGuard = businessAccessGuard;
    }

    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> findAll(String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        return subscriptionPlanRepository.findAllByDeletedAtIsNullOrderByMonthlyPriceAscNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubscriptionPlanResponse findById(Integer id, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        return toResponse(findPlan(id));
    }

    @Transactional
    public SubscriptionPlanResponse create(CreateSubscriptionPlanRequest request, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        String code = normalizeCode(request.code());
        if (subscriptionPlanRepository.existsByCodeAndDeletedAtIsNull(code)) {
            throw new ConflictException("subscription_plan.code.exists");
        }

        SubscriptionPlan plan = new SubscriptionPlan();
        apply(plan, code, request.name(), request.monthlyPrice(), request.yearlyPrice(),
                request.includedUsers(), request.maxBusinesses(), request.maxBranches(), request.features(), request.active());

        return toResponse(subscriptionPlanRepository.save(plan));
    }

    @Transactional
    public SubscriptionPlanResponse update(Integer id, UpdateSubscriptionPlanRequest request, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        SubscriptionPlan plan = findPlan(id);
        String code = normalizeCode(request.code());
        if (subscriptionPlanRepository.existsByCodeAndDeletedAtIsNullAndIdNot(code, id)) {
            throw new ConflictException("subscription_plan.code.exists");
        }

        apply(plan, code, request.name(), request.monthlyPrice(), request.yearlyPrice(),
                request.includedUsers(), request.maxBusinesses(), request.maxBranches(), request.features(), request.active());

        if (Boolean.FALSE.equals(request.active())) {
            plan.setDeletedAt(LocalDateTime.now());
        } else {
            plan.setDeletedAt(null);
        }

        return toResponse(plan);
    }

    @Transactional
    public void delete(Integer id, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        SubscriptionPlan plan = findPlan(id);
        plan.setActive(false);
        plan.setDeletedAt(LocalDateTime.now());
    }

    SubscriptionPlan findActivePlan(Integer id) {
        return subscriptionPlanRepository.findByIdAndDeletedAtIsNull(id)
                .filter(plan -> Boolean.TRUE.equals(plan.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("subscription_plan.not_found"));
    }

    private SubscriptionPlan findPlan(Integer id) {
        return subscriptionPlanRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("subscription_plan.not_found"));
    }

    private void apply(
            SubscriptionPlan plan,
            String code,
            String name,
            java.math.BigDecimal monthlyPrice,
            java.math.BigDecimal yearlyPrice,
            Integer includedUsers,
            Integer maxBusinesses,
            Integer maxBranches,
            String features,
            Boolean active) {
        plan.setCode(code);
        plan.setName(name.trim());
        plan.setMonthlyPrice(monthlyPrice.setScale(2, RoundingMode.HALF_UP));
        plan.setYearlyPrice(yearlyPrice.setScale(2, RoundingMode.HALF_UP));
        plan.setIncludedUsers(includedUsers);
        plan.setMaxBusinesses(maxBusinesses);
        plan.setMaxBranches(maxBranches);
        plan.setFeatures(features.trim());
        plan.setActive(active == null || active);
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }

    private SubscriptionPlanResponse toResponse(SubscriptionPlan plan) {
        return new SubscriptionPlanResponse(
                plan.getId(),
                plan.getCode(),
                plan.getName(),
                plan.getMonthlyPrice(),
                plan.getYearlyPrice(),
                plan.getIncludedUsers(),
                plan.getMaxBusinesses(),
                plan.getMaxBranches(),
                plan.getFeatures(),
                plan.getActive(),
                plan.getCreatedAt(),
                plan.getUpdatedAt());
    }
}
