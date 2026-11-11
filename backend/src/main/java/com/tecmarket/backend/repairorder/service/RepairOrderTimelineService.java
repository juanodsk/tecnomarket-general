package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.repairorder.entity.RepairOrderHistory;
import com.tecmarket.backend.repairorder.entity.RepairOrderHistoryVisibility;
import com.tecmarket.backend.repairorder.repository.RepairOrderHistoryRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class RepairOrderTimelineService {

    private final RepairOrderHistoryRepository historyRepository;
    private final AppUserRepository appUserRepository;

    public RepairOrderTimelineService(
            RepairOrderHistoryRepository historyRepository,
            AppUserRepository appUserRepository) {
        this.historyRepository = historyRepository;
        this.appUserRepository = appUserRepository;
    }

    public void recordInternalEvent(RepairOrder repairOrder, String subject, String comment) {
        AppUser actor = appUserRepository.findById(parseUserId(subject))
                .filter(user -> Boolean.TRUE.equals(user.getActive()) && user.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));

        RepairOrderHistory history = new RepairOrderHistory();
        history.setRepairOrder(repairOrder);
        history.setStatus(repairOrder.getStatus());
        history.setCreatedByUser(actor);
        history.setComment(comment);
        history.setVisibility(RepairOrderHistoryVisibility.INTERNAL);
        history.setActive(true);
        history.setDeletedAt(null);
        historyRepository.save(history);
    }

    private Integer parseUserId(String subject) {
        try {
            return Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }
    }
}
