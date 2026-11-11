package com.tecmarket.backend.appointment.service;

import com.tecmarket.backend.appointment.dto.AppointmentResponse;
import com.tecmarket.backend.appointment.dto.CreateAppointmentRequest;
import com.tecmarket.backend.appointment.dto.UpdateAppointmentRequest;
import com.tecmarket.backend.appointment.dto.UpdateAppointmentStatusRequest;
import com.tecmarket.backend.appointment.entity.Appointment;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.entity.BusinessOffering;
import com.tecmarket.backend.repairorder.entity.CustomerDevice;
import com.tecmarket.backend.appointment.mapper.AppointmentMapper;
import com.tecmarket.backend.appointment.repository.AppointmentRepository;
import com.tecmarket.backend.business.repository.BusinessOfferingRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.repairorder.repository.CustomerDeviceRepository;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.business.security.BusinessBranchAccessGuard;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BusinessRepository businessRepository;
    private final AppUserRepository appUserRepository;
    private final CustomerDeviceRepository customerDeviceRepository;
    private final BusinessOfferingRepository businessOfferingRepository;
    private final BusinessCatalogLookupService businessCatalogLookupService;
    private final AppointmentMapper appointmentMapper;
    private final BusinessBranchRepository businessBranchRepository;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchAccessGuard businessBranchAccessGuard;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            BusinessRepository businessRepository,
            AppUserRepository appUserRepository,
            CustomerDeviceRepository customerDeviceRepository,
            BusinessOfferingRepository businessOfferingRepository,
            BusinessCatalogLookupService businessCatalogLookupService,
            AppointmentMapper appointmentMapper,
            BusinessBranchRepository businessBranchRepository,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchAccessGuard businessBranchAccessGuard) {
        this.appointmentRepository = appointmentRepository;
        this.businessRepository = businessRepository;
        this.appUserRepository = appUserRepository;
        this.customerDeviceRepository = customerDeviceRepository;
        this.businessOfferingRepository = businessOfferingRepository;
        this.businessCatalogLookupService = businessCatalogLookupService;
        this.appointmentMapper = appointmentMapper;
        this.businessBranchRepository = businessBranchRepository;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchAccessGuard = businessBranchAccessGuard;
    }

    @Transactional(readOnly = true)
    public PageResponse<AppointmentResponse> findAllByBusiness(
            Integer businessId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "APPOINTMENT_VIEW");
        findBusiness(businessId);
        return PageResponse.from(appointmentRepository
                .findByBusinessIdAndBranchIdInAndDeletedAtIsNullOrderByScheduledAtDescIdDesc(
                        businessId,
                        businessBranchAccessGuard.accessibleBranchIds(businessId, subject),
                        PageRequestFactory.of(page, size))
                .map(appointmentMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findById(Integer businessId, Integer appointmentId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "APPOINTMENT_VIEW");
        Appointment appointment = appointmentRepository.findByIdAndBusinessIdAndDeletedAtIsNull(appointmentId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment.not_found"));
        businessBranchAccessGuard.findAccessibleBranch(businessId, appointment.getBranch().getId(), subject);
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse create(Integer businessId, CreateAppointmentRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "APPOINTMENT_CREATE");
        Business business = findBusiness(businessId);
        BusinessBranch branch = businessBranchAccessGuard.findAccessibleBranch(businessId, request.branchId(), subject);
        AppUser customer = findActiveUser(request.customerId());
        CustomerDevice customerDevice = findActiveCustomerDevice(businessId, request.customerDeviceId());
        validateCustomerDeviceOwnership(customerDevice, customer);
        BusinessOffering businessService = findActiveBusinessService(businessId, request.businessServiceId());
        BusinessCatalogItem status = findActiveAppointmentStatus(businessId, request.statusId());

        Appointment appointment = new Appointment();
        appointment.setBusiness(business);
        appointment.setBranch(branch);
        appointment.setCustomer(customer);
        appointment.setCustomerDevice(customerDevice);
        appointment.setBusinessService(businessService);
        appointment.setStatus(status);
        appointment.setScheduledAt(request.scheduledAt());
        appointment.setProblemDescription(request.problemDescription().trim());
        appointment.setActive(true);

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse update(Integer businessId, Integer appointmentId, UpdateAppointmentRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "APPOINTMENT_UPDATE");
        Appointment appointment = appointmentRepository.findByIdAndBusinessIdAndDeletedAtIsNull(appointmentId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment.not_found"));
        businessBranchAccessGuard.findAccessibleBranch(businessId, appointment.getBranch().getId(), subject);

        AppUser customer = findActiveUser(request.customerId());
        CustomerDevice customerDevice = findActiveCustomerDevice(businessId, request.customerDeviceId());
        validateCustomerDeviceOwnership(customerDevice, customer);
        BusinessOffering businessService = findActiveBusinessService(businessId, request.businessServiceId());
        BusinessCatalogItem status = findActiveAppointmentStatus(businessId, request.statusId());

        appointment.setCustomer(customer);
        appointment.setCustomerDevice(customerDevice);
        appointment.setBusinessService(businessService);
        appointment.setStatus(status);
        appointment.setScheduledAt(request.scheduledAt());
        appointment.setProblemDescription(request.problemDescription().trim());

        if (request.active() != null) {
            appointment.setActive(request.active());
            appointment.setDeletedAt(Boolean.FALSE.equals(request.active()) ? LocalDateTime.now() : null);
        }

        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public void delete(Integer businessId, Integer appointmentId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "APPOINTMENT_UPDATE");
        Appointment appointment = appointmentRepository.findByIdAndBusinessIdAndDeletedAtIsNull(appointmentId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment.not_found"));
        businessBranchAccessGuard.findAccessibleBranch(businessId, appointment.getBranch().getId(), subject);

        appointment.setActive(false);
        appointment.setDeletedAt(LocalDateTime.now());
    }

    @Transactional
    public AppointmentResponse updateStatus(
            Integer businessId,
            Integer appointmentId,
            UpdateAppointmentStatusRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "APPOINTMENT_UPDATE");
        Appointment appointment = appointmentRepository.findByIdAndBusinessIdAndDeletedAtIsNull(appointmentId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("appointment.not_found"));
        businessBranchAccessGuard.findAccessibleBranch(businessId, appointment.getBranch().getId(), subject);

        BusinessCatalogItem status = findActiveAppointmentStatus(businessId, request.statusId());
        appointment.setStatus(status);

        return appointmentMapper.toResponse(appointment);
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private BusinessBranch findActiveBranch(Integer businessId, Integer branchId) {
        return businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(branchId, businessId)
                .filter(branch -> Boolean.TRUE.equals(branch.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_branch.not_found"));
    }

    private AppUser findActiveUser(Integer userId) {
        return appUserRepository.findById(userId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()) && value.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));
    }

    private CustomerDevice findActiveCustomerDevice(Integer businessId, Integer customerDeviceId) {
        return customerDeviceRepository.findByIdAndBusinessIdAndDeletedAtIsNull(customerDeviceId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("customer_device.not_found"));
    }

    private BusinessOffering findActiveBusinessService(Integer businessId, Integer businessServiceId) {
        return businessOfferingRepository.findByIdAndBusinessIdAndDeletedAtIsNull(businessServiceId, businessId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_service.not_found"));
    }

    private BusinessCatalogItem findActiveAppointmentStatus(Integer businessId, Integer statusId) {
        return businessCatalogLookupService.findActive(
                businessId,
                statusId,
                BusinessCatalogType.APPOINTMENT_STATUS);
    }

    private void validateCustomerDeviceOwnership(CustomerDevice customerDevice, AppUser customer) {
        if (!customerDevice.getCustomer().getId().equals(customer.getId())) {
            throw new ConflictException("appointment.customer_device_mismatch");
        }
    }
}
