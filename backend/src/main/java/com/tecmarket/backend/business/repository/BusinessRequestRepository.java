package com.tecmarket.backend.business.repository;

import com.tecmarket.backend.business.entity.BusinessRequest;
import com.tecmarket.backend.business.entity.BusinessRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface BusinessRequestRepository extends JpaRepository<BusinessRequest, Integer> {

    Page<BusinessRequest> findByRequestedByUserIdAndActiveTrueOrderByCreatedAtDesc(
            Integer requestedByUserId, Pageable pageable);

    Page<BusinessRequest> findByStatusAndActiveTrueOrderByCreatedAtAsc(
            BusinessRequestStatus status, Pageable pageable);

    Optional<BusinessRequest> findByIdAndActiveTrue(Integer id);

}
