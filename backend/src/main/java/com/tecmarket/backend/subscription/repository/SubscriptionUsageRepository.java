package com.tecmarket.backend.subscription.repository;

import com.tecmarket.backend.subscription.entity.SubscriptionUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionUsageRepository extends JpaRepository<SubscriptionUsage, Integer> {

    Optional<SubscriptionUsage> findByBusinessSubscriptionId(Integer businessSubscriptionId);
}
