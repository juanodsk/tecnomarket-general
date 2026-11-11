package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.auth.repository.RefreshTokenRepository;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityVersionService {

    private final AppUserRepository appUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public SecurityVersionService(
            AppUserRepository appUserRepository,
            RefreshTokenRepository refreshTokenRepository) {
        this.appUserRepository = appUserRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void invalidateUser(Integer userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));
        invalidateManagedUser(user, LocalDateTime.now());
    }

    @Transactional
    public void invalidateUser(AppUser user) {
        invalidateManagedUser(user, LocalDateTime.now());
    }

    @Transactional
    public void invalidateUsers(Collection<Integer> userIds) {
        List<Integer> distinctUserIds = new LinkedHashSet<>(userIds).stream().toList();
        if (distinctUserIds.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        appUserRepository.findAllById(distinctUserIds)
                .forEach(user -> invalidateManagedUser(user, now));
    }

    private void invalidateManagedUser(AppUser user, LocalDateTime revokedAt) {
        int currentVersion = user.getSecurityVersion() == null ? 0 : user.getSecurityVersion();
        if (currentVersion == Integer.MAX_VALUE) {
            throw new IllegalStateException("User security version is exhausted.");
        }
        user.setSecurityVersion(currentVersion + 1);
        refreshTokenRepository.revokeAllByUserId(user.getId(), revokedAt);
    }
}
