package com.tecmarket.backend.subscription.service;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.subscription.dto.BusinessSubscriptionResponse;
import com.tecmarket.backend.subscription.dto.CreateBusinessSubscriptionRequest;
import com.tecmarket.backend.subscription.dto.SubscriptionUsageResponse;
import com.tecmarket.backend.subscription.dto.UpdateBusinessSubscriptionRequest;
import com.tecmarket.backend.subscription.entity.BusinessSubscription;
import com.tecmarket.backend.subscription.entity.SubscriptionPlan;
import com.tecmarket.backend.subscription.entity.SubscriptionStatus;
import com.tecmarket.backend.subscription.entity.SubscriptionUsage;
import com.tecmarket.backend.subscription.repository.BusinessSubscriptionRepository;
import com.tecmarket.backend.subscription.repository.SubscriptionUsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusinessSubscriptionService {

    private final BusinessSubscriptionRepository businessSubscriptionRepository;
    private final SubscriptionUsageRepository subscriptionUsageRepository;
    private final BusinessRepository businessRepository;
    private final BusinessBranchRepository businessBranchRepository;
    private final BusinessUserRepository businessUserRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final SubscriptionPlanService subscriptionPlanService;
    private final BusinessAccessGuard businessAccessGuard;

    public BusinessSubscriptionService(
            BusinessSubscriptionRepository businessSubscriptionRepository,
            SubscriptionUsageRepository subscriptionUsageRepository,
            BusinessRepository businessRepository,
            BusinessBranchRepository businessBranchRepository,
            BusinessUserRepository businessUserRepository,
            RepairOrderRepository repairOrderRepository,
            SubscriptionPlanService subscriptionPlanService,
            BusinessAccessGuard businessAccessGuard) {
        this.businessSubscriptionRepository = businessSubscriptionRepository;
        this.subscriptionUsageRepository = subscriptionUsageRepository;
        this.businessRepository = businessRepository;
        this.businessBranchRepository = businessBranchRepository;
        this.businessUserRepository = businessUserRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.subscriptionPlanService = subscriptionPlanService;
        this.businessAccessGuard = businessAccessGuard;
    }

    @Transactional(readOnly = true)
    public List<BusinessSubscriptionResponse> findAll(String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        return businessSubscriptionRepository.findAllByDeletedAtIsNullOrderByIdAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BusinessSubscriptionResponse findById(Integer id, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        return toResponse(findSubscription(id));
    }

    @Transactional(readOnly = true)
    public BusinessSubscriptionResponse findActiveByBusiness(Integer businessId, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        return businessSubscriptionRepository.findByBusinessIdAndActiveTrueAndDeletedAtIsNull(businessId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("business_subscription.not_found"));
    }

    @Transactional
    public BusinessSubscriptionResponse create(
            Integer businessId,
            CreateBusinessSubscriptionRequest request,
            String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        Business business = findBusiness(businessId);
        if (businessSubscriptionRepository.existsByBusinessIdAndActiveTrueAndDeletedAtIsNull(businessId)) {
            throw new ConflictException("business_subscription.active.exists");
        }

        SubscriptionPlan plan = subscriptionPlanService.findActivePlan(request.planId());
        BusinessSubscription subscription = new BusinessSubscription();
        subscription.setBusiness(business);
        apply(subscription, plan, request);

        BusinessSubscription saved = businessSubscriptionRepository.save(subscription);
        recalculateUsage(saved);
        return toResponse(saved);
    }

    @Transactional
    public BusinessSubscriptionResponse update(Integer id, UpdateBusinessSubscriptionRequest request, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        BusinessSubscription subscription = findSubscription(id);
        SubscriptionPlan plan = subscriptionPlanService.findActivePlan(request.planId());

        subscription.setPlan(plan);
        subscription.setStatus(request.status());
        subscription.setBillingCycle(request.billingCycle());
        subscription.setCurrentPeriodStart(request.currentPeriodStart());
        subscription.setCurrentPeriodEnd(request.currentPeriodEnd());
        subscription.setTrialEndsAt(request.trialEndsAt());
        subscription.setCancelAtPeriodEnd(request.cancelAtPeriodEnd());
        subscription.setPaymentProvider(request.paymentProvider());
        subscription.setProviderCustomerId(normalizeNullable(request.providerCustomerId()));
        subscription.setProviderSubscriptionId(normalizeNullable(request.providerSubscriptionId()));
        subscription.setActive(request.active());
        subscription.setDeletedAt(Boolean.FALSE.equals(request.active()) ? LocalDateTime.now() : null);

        recalculateUsage(subscription);
        return toResponse(subscription);
    }

    @Transactional
    public void cancel(Integer id, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        BusinessSubscription subscription = findSubscription(id);
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancelAtPeriodEnd(true);
        subscription.setActive(false);
        subscription.setDeletedAt(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public SubscriptionUsageResponse findUsage(Integer id, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        BusinessSubscription subscription = findSubscription(id);
        SubscriptionUsage usage = subscriptionUsageRepository.findByBusinessSubscriptionId(subscription.getId())
                .orElseThrow(() -> new ResourceNotFoundException("subscription_usage.not_found"));
        return toUsageResponse(usage);
    }

    @Transactional
    public SubscriptionUsageResponse recalculateUsage(Integer id, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        BusinessSubscription subscription = findSubscription(id);
        return toUsageResponse(recalculateUsage(subscription));
    }

    private void apply(BusinessSubscription subscription, SubscriptionPlan plan, CreateBusinessSubscriptionRequest request) {
        subscription.setPlan(plan);
        subscription.setStatus(request.status());
        subscription.setBillingCycle(request.billingCycle());
        subscription.setCurrentPeriodStart(request.currentPeriodStart());
        subscription.setCurrentPeriodEnd(request.currentPeriodEnd());
        subscription.setTrialEndsAt(request.trialEndsAt());
        subscription.setCancelAtPeriodEnd(request.cancelAtPeriodEnd() != null && request.cancelAtPeriodEnd());
        subscription.setPaymentProvider(request.paymentProvider());
        subscription.setProviderCustomerId(normalizeNullable(request.providerCustomerId()));
        subscription.setProviderSubscriptionId(normalizeNullable(request.providerSubscriptionId()));
        subscription.setActive(request.active() == null || request.active());
        subscription.setDeletedAt(Boolean.FALSE.equals(request.active()) ? LocalDateTime.now() : null);
    }

    private SubscriptionUsage recalculateUsage(BusinessSubscription subscription) {
        SubscriptionUsage usage = subscriptionUsageRepository.findByBusinessSubscriptionId(subscription.getId())
                .orElseGet(SubscriptionUsage::new);
        usage.setBusinessSubscription(subscription);
        usage.setUsersCount(safeLongToInt(businessUserRepository.countByBusinessIdAndActiveTrueAndDeletedAtIsNull(
                subscription.getBusiness().getId())));
        usage.setOrdersCount(safeLongToInt(repairOrderRepository.countByBusinessIdAndDeletedAtIsNull(
                subscription.getBusiness().getId())));
        usage.setBranchesCount(safeLongToInt(businessBranchRepository.countByBusinessIdAndActiveTrueAndDeletedAtIsNull(
                subscription.getBusiness().getId())));
        if (usage.getStorageUsed() == null) {
            usage.setStorageUsed(0L);
        }
        return subscriptionUsageRepository.save(usage);
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private BusinessSubscription findSubscription(Integer id) {
        return businessSubscriptionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("business_subscription.not_found"));
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private int safeLongToInt(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) value;
    }

    private BusinessSubscriptionResponse toResponse(BusinessSubscription subscription) {
        return new BusinessSubscriptionResponse(
                subscription.getId(),
                subscription.getBusiness().getId(),
                subscription.getBusiness().getName(),
                subscription.getPlan().getId(),
                subscription.getPlan().getCode(),
                subscription.getPlan().getName(),
                subscription.getStatus(),
                subscription.getBillingCycle(),
                subscription.getCurrentPeriodStart(),
                subscription.getCurrentPeriodEnd(),
                subscription.getTrialEndsAt(),
                subscription.getCancelAtPeriodEnd(),
                subscription.getPaymentProvider(),
                subscription.getProviderCustomerId(),
                subscription.getProviderSubscriptionId(),
                subscription.getActive(),
                subscription.getCreatedAt(),
                subscription.getUpdatedAt());
    }

    private SubscriptionUsageResponse toUsageResponse(SubscriptionUsage usage) {
        return new SubscriptionUsageResponse(
                usage.getId(),
                usage.getBusinessSubscription().getId(),
                usage.getUsersCount(),
                usage.getOrdersCount(),
                usage.getBranchesCount(),
                usage.getStorageUsed(),
                usage.getUpdatedAt());
    }
}
