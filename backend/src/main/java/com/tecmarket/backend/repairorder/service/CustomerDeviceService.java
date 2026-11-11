package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.repairorder.dto.CreateCustomerDeviceRequest;
import com.tecmarket.backend.repairorder.dto.CustomerDeviceResponse;
import com.tecmarket.backend.repairorder.dto.QuickRepairOrderDeviceRequest;
import com.tecmarket.backend.repairorder.dto.UpdateCustomerDeviceRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.repairorder.entity.CustomerDevice;
import com.tecmarket.backend.repairorder.mapper.CustomerDeviceMapper;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.repairorder.repository.CustomerDeviceRepository;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.List;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
@Service
public class CustomerDeviceService {

    private final CustomerDeviceRepository customerDeviceRepository;
    private final BusinessRepository businessRepository;
    private final AppUserRepository appUserRepository;
    private final BusinessCatalogLookupService businessCatalogLookupService;
    private final BusinessUserRepository businessUserRepository;
    private final CustomerDeviceMapper customerDeviceMapper;
    private final BusinessAccessGuard businessAccessGuard;

    public CustomerDeviceService(
            CustomerDeviceRepository customerDeviceRepository,
            BusinessRepository businessRepository,
            AppUserRepository appUserRepository,
            BusinessCatalogLookupService businessCatalogLookupService,
            BusinessUserRepository businessUserRepository,
            CustomerDeviceMapper customerDeviceMapper,
            BusinessAccessGuard businessAccessGuard) {
        this.customerDeviceRepository = customerDeviceRepository;
        this.businessRepository = businessRepository;
        this.appUserRepository = appUserRepository;
        this.businessCatalogLookupService = businessCatalogLookupService;
        this.businessUserRepository = businessUserRepository;
        this.customerDeviceMapper = customerDeviceMapper;
        this.businessAccessGuard = businessAccessGuard;
    }

    @Transactional(readOnly = true)
    public PageResponse<CustomerDeviceResponse> findAllByBusiness(
            Integer businessId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "CUSTOMER_DEVICE_VIEW");
        findBusiness(businessId);
        return PageResponse.from(customerDeviceRepository
                .findByBusinessIdAndDeletedAtIsNullOrderByRegisteredAtDescIdDesc(
                        businessId, PageRequestFactory.of(page, size))
                .map(customerDeviceMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public CustomerDeviceResponse findById(Integer businessId, Integer customerDeviceId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "CUSTOMER_DEVICE_VIEW");
        CustomerDevice customerDevice = customerDeviceRepository.findByIdAndBusinessIdAndDeletedAtIsNull(
                customerDeviceId,
                businessId).orElseThrow(() -> new ResourceNotFoundException("customer_device.not_found"));
        return customerDeviceMapper.toResponse(customerDevice);
    }

    @Transactional
    public CustomerDeviceResponse create(Integer businessId, CreateCustomerDeviceRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "CUSTOMER_DEVICE_CREATE");
        Business business = findBusiness(businessId);
        AppUser customer = findActiveUser(request.customerId());
        BusinessCatalogItem deviceType = findActiveDeviceType(businessId, request.deviceTypeId());
        BusinessCatalogItem brand = findActiveBrand(businessId, request.brandId());
        BusinessUser registeredByBusinessUser = findActiveBusinessUser(businessId, request.registeredByBusinessUserId());

        String normalizedImei = normalizeNullable(request.imei());
        validateImeiUniqueness(businessId, customer.getId(), normalizedImei, null);

        CustomerDevice entity = new CustomerDevice();
        entity.setBusiness(business);
        entity.setCustomer(customer);
        entity.setDeviceType(deviceType);
        entity.setBrand(brand);
        entity.setModel(request.model().trim());
        entity.setSerialNumber(normalizeNullable(request.serialNumber()));
        entity.setImei(normalizedImei);
        entity.setRegisteredByBusinessUser(registeredByBusinessUser);
        entity.setColor(request.color().trim());
        entity.setRegisteredAt(request.registeredAt());
        entity.setActive(true);

        return customerDeviceMapper.toResponse(customerDeviceRepository.save(entity));
    }

    @Transactional
    public CustomerDeviceResponse resolveOrCreateForQuickOrder(
            Integer businessId,
            Integer customerId,
            QuickRepairOrderDeviceRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "CUSTOMER_DEVICE_CREATE");
        Business business = findBusiness(businessId);
        AppUser customer = findActiveUser(customerId);
        BusinessCatalogItem deviceType = findActiveDeviceType(businessId, request.deviceTypeId());
        BusinessCatalogItem brand = findActiveBrand(businessId, request.brandId());
        BusinessUser registeredBy = findActiveBusinessUserByUserId(businessId, parseUserId(subject));
        String normalizedImei = normalizeNullable(request.imei());

        if (StringUtils.hasText(normalizedImei)) {
            List<CustomerDevice> matches = customerDeviceRepository
                    .findByBusinessIdAndImeiAndActiveTrueAndDeletedAtIsNullOrderByIdAsc(
                            businessId, normalizedImei);
            if (!matches.isEmpty()) {
                CustomerDevice existing = matches.get(0);
                if (matches.size() > 1 || !existing.getCustomer().getId().equals(customerId)) {
                    throw new ConflictException("quick_order.device.imei.customer_conflict");
                }
                if (!existing.getDeviceType().getId().equals(deviceType.getId())
                        || !existing.getBrand().getId().equals(brand.getId())
                        || !existing.getModel().equalsIgnoreCase(request.model().trim())) {
                    throw new ConflictException("quick_order.device.imei.details_conflict");
                }
                return customerDeviceMapper.toResponse(existing);
            }
        }

        CustomerDevice entity = new CustomerDevice();
        entity.setBusiness(business);
        entity.setCustomer(customer);
        entity.setDeviceType(deviceType);
        entity.setBrand(brand);
        entity.setModel(request.model().trim());
        entity.setSerialNumber(normalizeNullable(request.serialNumber()));
        entity.setImei(normalizedImei);
        entity.setRegisteredByBusinessUser(registeredBy);
        entity.setColor(request.color().trim());
        entity.setRegisteredAt(request.registeredAt());
        entity.setActive(true);
        return customerDeviceMapper.toResponse(customerDeviceRepository.save(entity));
    }

    @Transactional
    public CustomerDeviceResponse update(
            Integer businessId,
            Integer customerDeviceId,
            UpdateCustomerDeviceRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "CUSTOMER_DEVICE_UPDATE");
        CustomerDevice entity = customerDeviceRepository.findByIdAndBusinessIdAndDeletedAtIsNull(customerDeviceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("customer_device.not_found"));

        AppUser customer = findActiveUser(request.customerId());
        BusinessCatalogItem deviceType = findActiveDeviceType(businessId, request.deviceTypeId());
        BusinessCatalogItem brand = findActiveBrand(businessId, request.brandId());
        BusinessUser registeredByBusinessUser = findActiveBusinessUser(businessId, request.registeredByBusinessUserId());

        String normalizedImei = normalizeNullable(request.imei());
        validateImeiUniqueness(businessId, customer.getId(), normalizedImei, customerDeviceId);

        entity.setCustomer(customer);
        entity.setDeviceType(deviceType);
        entity.setBrand(brand);
        entity.setModel(request.model().trim());
        entity.setSerialNumber(normalizeNullable(request.serialNumber()));
        entity.setImei(normalizedImei);
        entity.setRegisteredByBusinessUser(registeredByBusinessUser);
        entity.setColor(request.color().trim());
        entity.setRegisteredAt(request.registeredAt() != null ? request.registeredAt() : entity.getRegisteredAt());

        if (request.active() != null) {
            entity.setActive(request.active());
            entity.setDeletedAt(Boolean.FALSE.equals(request.active()) ? LocalDateTime.now() : null);
        }

        return customerDeviceMapper.toResponse(entity);
    }

    @Transactional
    public void delete(Integer businessId, Integer customerDeviceId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "CUSTOMER_DEVICE_UPDATE");
        CustomerDevice entity = customerDeviceRepository.findByIdAndBusinessIdAndDeletedAtIsNull(customerDeviceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("customer_device.not_found"));

        entity.setActive(false);
        entity.setDeletedAt(LocalDateTime.now());
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private AppUser findActiveUser(Integer userId) {
        return appUserRepository.findById(userId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));
    }

    private BusinessCatalogItem findActiveDeviceType(Integer businessId, Integer deviceTypeId) {
        return businessCatalogLookupService.findActive(
                businessId,
                deviceTypeId,
                BusinessCatalogType.DEVICE_TYPE);
    }

    private BusinessCatalogItem findActiveBrand(Integer businessId, Integer brandId) {
        return businessCatalogLookupService.findActive(
                businessId,
                brandId,
                BusinessCatalogType.BRAND);
    }

    private BusinessUser findActiveBusinessUser(Integer businessId, Integer businessUserId) {
        return businessUserRepository.findByIdAndBusinessIdAndDeletedAtIsNull(businessUserId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));
    }

    private BusinessUser findActiveBusinessUserByUserId(Integer businessId, Integer userId) {
        return businessUserRepository.findByBusinessIdAndUserIdAndActiveTrueAndDeletedAtIsNull(businessId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("business_user.not_found"));
    }

    private void validateImeiUniqueness(Integer businessId, Integer customerId, String imei, Integer idToExclude) {
        if (!StringUtils.hasText(imei)) {
            return;
        }

        boolean exists = (idToExclude == null)
                ? customerDeviceRepository.existsByBusinessIdAndCustomerIdAndImeiAndDeletedAtIsNull(
                        businessId,
                        customerId,
                        imei)
                : customerDeviceRepository.existsByBusinessIdAndCustomerIdAndImeiAndDeletedAtIsNullAndIdNot(
                        businessId,
                        customerId,
                        imei,
                        idToExclude);

        if (exists) {
            throw new ConflictException("customer_device.imei.exists");
        }
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private Integer parseUserId(String subject) {
        try {
            return Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }
    }
}
