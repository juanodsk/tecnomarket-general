package com.tecmarket.backend.parameter.service;

import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.parameter.dto.UserParameterResponse;
import com.tecmarket.backend.parameter.mapper.UserParameterMapper;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.auth.service.SecurityVersionService;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.parameter.dto.UpdateUserStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserParameterService {

    private final AppUserRepository appUserRepository;
    private final UserParameterMapper userParameterMapper;
    private final BusinessAccessGuard businessAccessGuard;
    private final SecurityVersionService securityVersionService;

    public UserParameterService(
            AppUserRepository appUserRepository,
            UserParameterMapper userParameterMapper,
            BusinessAccessGuard businessAccessGuard,
            SecurityVersionService securityVersionService) {
        this.appUserRepository = appUserRepository;
        this.userParameterMapper = userParameterMapper;
        this.businessAccessGuard = businessAccessGuard;
        this.securityVersionService = securityVersionService;
    }

    @Transactional(readOnly = true)
    public PageResponse<UserParameterResponse> findAll(Integer page, Integer size) {
        return PageResponse.from(appUserRepository
                .findByActiveTrueAndDeletedAtIsNullOrderByIdAsc(PageRequestFactory.of(page, size))
                .map(userParameterMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public UserParameterResponse findById(Integer id) {
        AppUser user = appUserRepository.findById(id)
                .filter(appUser -> Boolean.TRUE.equals(appUser.getActive()))
                .filter(appUser -> appUser.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));

        return userParameterMapper.toResponse(user);
    }

    @Transactional
    public UserParameterResponse updateStatus(
            Integer userId,
            UpdateUserStatusRequest request,
            String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        AppUser user = appUserRepository.findById(userId)
                .filter(appUser -> appUser.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));

        if (!request.active().equals(user.getActive())) {
            user.setActive(request.active());
            securityVersionService.invalidateUser(user);
        }
        return userParameterMapper.toResponse(user);
    }

    @Transactional
    public void revokeAllSessions(Integer userId, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        securityVersionService.invalidateUser(userId);
    }
}
