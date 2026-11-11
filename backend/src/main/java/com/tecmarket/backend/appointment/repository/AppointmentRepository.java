package com.tecmarket.backend.appointment.repository;

import com.tecmarket.backend.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByBusinessIdAndDeletedAtIsNullOrderByScheduledAtDescIdDesc(Integer businessId);

    Page<Appointment> findByBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByScheduledAtDescIdDesc(
            Integer businessId, List<Integer> branchIds, Pageable pageable);

    Optional<Appointment> findByIdAndBusinessIdAndDeletedAtIsNull(Integer id, Integer businessId);
}
