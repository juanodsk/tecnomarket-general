package com.tecmarket.backend.subscription.repository;

import com.tecmarket.backend.subscription.entity.BusinessSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessSubscriptionRepository extends JpaRepository<BusinessSubscription, Integer> {

    List<BusinessSubscription> findAllByDeletedAtIsNullOrderByIdAsc();

    Optional<BusinessSubscription> findByIdAndDeletedAtIsNull(Integer id);

    Optional<BusinessSubscription> findByBusinessIdAndActiveTrueAndDeletedAtIsNull(Integer businessId);

    boolean existsByBusinessIdAndActiveTrueAndDeletedAtIsNull(Integer businessId);
}
