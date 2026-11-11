package com.tecmarket.backend.seed;

import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.entity.Country;
import com.tecmarket.backend.parameter.entity.Department;
import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.parameter.repository.CountryRepository;
import com.tecmarket.backend.parameter.repository.DepartmentRepository;
import com.tecmarket.backend.permission.entity.AppModule;
import com.tecmarket.backend.permission.entity.Permission;
import com.tecmarket.backend.permission.entity.PermissionScope;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.entity.RolePermission;
import com.tecmarket.backend.permission.repository.AppModuleRepository;
import com.tecmarket.backend.permission.repository.PermissionRepository;
import com.tecmarket.backend.permission.repository.RolePermissionRepository;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.entity.UserRole;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.user.repository.DocumentTypeRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GeneralDataSeeder implements ApplicationRunner {

    private static final Set<String> GLOBAL_PERMISSION_CODES = Set.of(
            "PARAMETER_VIEW",
            "PARAMETER_MANAGE",
            "BUSINESS_REQUEST_REVIEW",
            "SUBSCRIPTION_VIEW",
            "SUBSCRIPTION_MANAGE");

    private final DocumentTypeRepository documentTypeRepository;
    private final AppUserRepository appUserRepository;
    private final UserRoleRepository userRoleRepository;
    private final AppModuleRepository appModuleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final CountryRepository countryRepository;
    private final DepartmentRepository departmentRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:false}")
    private boolean enabled;

    @Value("${app.seed.platform-admin.enabled:false}")
    private boolean platformAdminEnabled;

    @Value("${app.seed.platform-admin.email:}")
    private String platformAdminEmail;

    @Value("${app.seed.platform-admin.password:}")
    private String platformAdminPassword;

    @Value("${app.seed.platform-admin.document-type-code:}")
    private String platformAdminDocumentTypeCode;

    @Value("${app.seed.platform-admin.document-number:}")
    private String platformAdminDocumentNumber;

    @Value("${app.seed.platform-admin.name:}")
    private String platformAdminName;

    @Value("${app.seed.platform-admin.surname:}")
    private String platformAdminSurname;

    @Value("${app.seed.platform-admin.address:}")
    private String platformAdminAddress;

    @Value("${app.seed.platform-admin.phone:}")
    private String platformAdminPhone;

    @Value("${app.seed.platform-admin.city:}")
    private String platformAdminCity;

    public GeneralDataSeeder(
            DocumentTypeRepository documentTypeRepository,
            AppUserRepository appUserRepository,
            UserRoleRepository userRoleRepository,
            AppModuleRepository appModuleRepository,
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            RolePermissionRepository rolePermissionRepository,
            CountryRepository countryRepository,
            DepartmentRepository departmentRepository,
            CityRepository cityRepository,
            PasswordEncoder passwordEncoder) {
        this.documentTypeRepository = documentTypeRepository;
        this.appUserRepository = appUserRepository;
        this.userRoleRepository = userRoleRepository;
        this.appModuleRepository = appModuleRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.countryRepository = countryRepository;
        this.departmentRepository = departmentRepository;
        this.cityRepository = cityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }

        seedSecurity();
        seedParameters();
        if (platformAdminEnabled) {
            seedPlatformAdmin();
        }
    }

    private void seedSecurity() {
        seedModules();
        seedPermissions();
        seedRoles();
        seedRolePermissions();
    }

    private void seedModules() {
        upsertModule("PARAMETER", "Parametrizacion");
        upsertModule("BUSINESS", "Negocios");
        upsertModule("USER_MANAGEMENT", "Gestion de usuarios");
        upsertModule("APPOINTMENT", "Citas");
        upsertModule("REPAIR_ORDER", "Ordenes de reparacion");
        upsertModule("QUOTATION", "Cotizaciones");
        upsertModule("INVENTORY", "Inventario");
        upsertModule("SALE", "Ventas");
        upsertModule("REVIEW", "Resenas");
        upsertModule("REPORT", "Reportes");
        upsertModule("COMMISSION", "Comisiones");
        upsertModule("SUBSCRIPTION", "Suscripciones");
    }

    private void seedPermissions() {
        upsertPermission("PARAMETER", "PARAMETER_VIEW", "Ver parametros", "Permite consultar parametros generales.");
        upsertPermission("PARAMETER", "PARAMETER_MANAGE", "Gestionar parametros",
                "Permite crear, actualizar y desactivar parametros.");
        upsertPermission("PARAMETER", "CATALOG_VIEW", "Ver catalogos operativos",
                "Permite consultar catalogos globales usados en la operacion de negocios sin acceso a parametrizacion.");

        upsertPermission("BUSINESS", "BUSINESS_VIEW", "Ver negocio", "Permite consultar informacion del negocio.");
        upsertPermission("BUSINESS", "BUSINESS_UPDATE", "Actualizar negocio",
                "Permite actualizar informacion del negocio.");
        upsertPermission("BUSINESS", "BUSINESS_REQUEST_CREATE", "Crear solicitud de negocio",
                "Permite crear solicitud para registrar un negocio.");
        upsertPermission("BUSINESS", "BUSINESS_REQUEST_VIEW_OWN", "Ver mis solicitudes de negocio",
                "Permite consultar solicitudes de negocio propias.");
        upsertPermission("BUSINESS", "BUSINESS_REQUEST_REVIEW", "Revisar solicitudes de negocio",
                "Permite listar, aprobar y rechazar solicitudes de negocio.");
        upsertPermission("BUSINESS", "BUSINESS_CATALOG_VIEW", "Ver catalogos del negocio",
                "Permite consultar catalogos configurables propios del negocio.");
        upsertPermission("BUSINESS", "BUSINESS_CATALOG_MANAGE", "Gestionar catalogos del negocio",
                "Permite crear, actualizar y desactivar catalogos configurables propios del negocio.");
        upsertPermission("BUSINESS", "BUSINESS_BRANCH_VIEW", "Ver sedes del negocio",
                "Permite consultar sedes o locales configurados por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_BRANCH_CREATE", "Crear sedes del negocio",
                "Permite crear sedes o locales configurados por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_BRANCH_UPDATE", "Actualizar sedes del negocio",
                "Permite actualizar o desactivar sedes o locales configurados por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_SERVICE_VIEW", "Ver servicios del negocio",
                "Permite consultar servicios configurados por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_SERVICE_CREATE", "Crear servicios del negocio",
                "Permite crear servicios configurados por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_SERVICE_UPDATE", "Actualizar servicios del negocio",
                "Permite actualizar o desactivar servicios configurados por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_SCHEDULE_VIEW", "Ver horarios del negocio",
                "Permite consultar horarios configurados por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_SCHEDULE_CREATE", "Crear horarios del negocio",
                "Permite crear horarios configurados por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_SCHEDULE_UPDATE", "Actualizar horarios del negocio",
                "Permite actualizar o desactivar horarios configurados por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_QUALITY_TEST_VIEW", "Ver pruebas de calidad del negocio",
                "Permite consultar pruebas de ingreso configuradas por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_QUALITY_TEST_CREATE", "Crear pruebas de calidad del negocio",
                "Permite crear pruebas de ingreso configuradas por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_QUALITY_TEST_UPDATE", "Actualizar pruebas de calidad del negocio",
                "Permite actualizar o desactivar pruebas de ingreso configuradas por negocio.");
        upsertPermission("BUSINESS", "BUSINESS_QUALITY_CHECK_VIEW", "Ver checklist de ingreso",
                "Permite consultar checklist por ticket u orden.");
        upsertPermission("BUSINESS", "BUSINESS_QUALITY_CHECK_UPDATE", "Gestionar checklist de ingreso",
                "Permite crear, actualizar y desactivar checklist por ticket u orden.");
        upsertPermission("BUSINESS", "CUSTOMER_DEVICE_VIEW", "Ver dispositivos de clientes",
                "Permite consultar dispositivos registrados por negocio.");
        upsertPermission("BUSINESS", "CUSTOMER_DEVICE_CREATE", "Crear dispositivos de clientes",
                "Permite registrar dispositivos de clientes por negocio.");
        upsertPermission("BUSINESS", "CUSTOMER_DEVICE_UPDATE", "Actualizar dispositivos de clientes",
                "Permite actualizar o desactivar dispositivos de clientes por negocio.");

        upsertPermission("USER_MANAGEMENT", "BUSINESS_USER_VIEW", "Ver usuarios del negocio",
                "Permite consultar usuarios internos.");
        upsertPermission("USER_MANAGEMENT", "BUSINESS_USER_CREATE", "Crear usuarios del negocio",
                "Permite crear usuarios internos.");
        upsertPermission("USER_MANAGEMENT", "BUSINESS_USER_UPDATE", "Actualizar usuarios del negocio",
                "Permite actualizar usuarios internos.");

        upsertPermission("APPOINTMENT", "APPOINTMENT_CREATE", "Crear citas", "Permite crear citas.");
        upsertPermission("APPOINTMENT", "APPOINTMENT_VIEW", "Ver citas", "Permite consultar citas.");
        upsertPermission("APPOINTMENT", "APPOINTMENT_UPDATE", "Actualizar citas", "Permite actualizar citas.");

        upsertPermission("REPAIR_ORDER", "REPAIR_ORDER_CREATE", "Crear ordenes",
                "Permite crear ordenes de reparacion.");
        upsertPermission("REPAIR_ORDER", "REPAIR_ORDER_VIEW", "Ver ordenes",
                "Permite consultar ordenes de reparacion.");
        upsertPermission("REPAIR_ORDER", "REPAIR_ORDER_UPDATE", "Actualizar ordenes",
                "Permite actualizar ordenes de reparacion.");
        upsertPermission("REPAIR_ORDER", "REPAIR_ORDER_FINANCIAL_UPDATE", "Actualizar valores de ordenes",
                "Permite actualizar el precio final de una orden de reparacion.");
        upsertPermission("REPAIR_ORDER", "REPAIR_ORDER_CLOSE", "Cerrar ordenes",
                "Permite cerrar ordenes de reparacion.");
        upsertPermission("REPAIR_ORDER", "REPAIR_ORDER_UPLOAD_EVIDENCE", "Subir evidencias",
                "Permite subir evidencias a una orden.");

        upsertPermission("QUOTATION", "QUOTATION_CREATE", "Crear cotizaciones", "Permite crear cotizaciones.");
        upsertPermission("QUOTATION", "QUOTATION_VIEW", "Ver cotizaciones", "Permite consultar cotizaciones.");
        upsertPermission("QUOTATION", "QUOTATION_UPDATE", "Actualizar cotizaciones",
                "Permite actualizar cotizaciones.");

        upsertPermission("INVENTORY", "INVENTORY_VIEW", "Ver inventario", "Permite consultar inventario.");
        upsertPermission("INVENTORY", "INVENTORY_COST_VIEW", "Ver costos de inventario",
                "Permite consultar costos y margenes del inventario.");
        upsertPermission("INVENTORY", "INVENTORY_CREATE", "Crear inventario", "Permite crear productos o repuestos.");
        upsertPermission("INVENTORY", "INVENTORY_UPDATE", "Actualizar inventario",
                "Permite actualizar productos o repuestos.");
        upsertPermission("INVENTORY", "INVENTORY_DELETE", "Eliminar inventario",
                "Permite desactivar productos o repuestos.");

        upsertPermission("SALE", "SALE_CREATE", "Crear ventas", "Permite crear ventas directas.");
        upsertPermission("SALE", "SALE_VIEW", "Ver ventas", "Permite consultar ventas directas.");
        upsertPermission("SALE", "SALE_COST_VIEW", "Ver costos de ventas",
                "Permite consultar costos y margenes historicos de ventas.");

        upsertPermission("REVIEW", "REVIEW_VIEW", "Ver resenas", "Permite consultar resenas.");
        upsertPermission("REVIEW", "REVIEW_CREATE", "Crear resenas", "Permite crear resenas.");

        upsertPermission("REPORT", "REPORT_VIEW", "Ver reportes", "Permite consultar reportes.");

        upsertPermission("COMMISSION", "BUSINESS_COMMISSION_VIEW", "Ver comisiones",
                "Permite consultar ordenes liquidables, comisiones y liquidaciones de tecnicos.");
        upsertPermission("COMMISSION", "BUSINESS_COMMISSION_MANAGE", "Gestionar comisiones",
                "Permite crear, aprobar y pagar liquidaciones de comisiones.");

        upsertPermission("SUBSCRIPTION", "SUBSCRIPTION_VIEW", "Ver suscripciones",
                "Permite consultar planes, suscripciones y uso de los negocios.");
        upsertPermission("SUBSCRIPTION", "SUBSCRIPTION_MANAGE", "Gestionar suscripciones",
                "Permite crear planes y administrar suscripciones de negocios.");
    }

    private void seedRoles() {
        upsertRole("CUSTOMER", "Cliente", "Cliente general de la plataforma.", "GLOBAL");
        upsertRole("PLATFORM_ADMIN", "Administrador de plataforma", "Administrador global de TecMarket.", "GLOBAL");
        upsertRole("BUSINESS_OWNER", "Dueno de negocio", "Propietario de un negocio.", "BUSINESS");
        upsertRole("BUSINESS_ADMIN", "Administrador de negocio", "Administrador interno de un negocio.", "BUSINESS");
        upsertRole("TECHNICIAN", "Tecnico", "Tecnico encargado de reparaciones.", "BUSINESS");
        upsertRole("RECEPTIONIST", "Asesor", "Asesor o recepcionista del negocio.", "BUSINESS");
        upsertRole("SELLER", "Vendedor", "Vendedor de accesorios y productos.", "BUSINESS");
    }

    private void seedRolePermissions() {
        List<String> allPermissions = permissionRepository.findAllByDeletedAtIsNullOrderByCodeAsc()
                .stream()
                .map(Permission::getCode)
                .toList();

        List<String> owner = List.of(
                "CATALOG_VIEW", "BUSINESS_VIEW", "BUSINESS_UPDATE", "BUSINESS_REQUEST_CREATE",
                "BUSINESS_REQUEST_VIEW_OWN",
                "BUSINESS_CATALOG_VIEW", "BUSINESS_CATALOG_MANAGE",
                "BUSINESS_BRANCH_VIEW", "BUSINESS_BRANCH_CREATE", "BUSINESS_BRANCH_UPDATE",
                "BUSINESS_SERVICE_VIEW", "BUSINESS_SERVICE_CREATE", "BUSINESS_SERVICE_UPDATE",
                "BUSINESS_SCHEDULE_VIEW", "BUSINESS_SCHEDULE_CREATE", "BUSINESS_SCHEDULE_UPDATE",
                "BUSINESS_QUALITY_TEST_VIEW", "BUSINESS_QUALITY_TEST_CREATE", "BUSINESS_QUALITY_TEST_UPDATE",
                "BUSINESS_QUALITY_CHECK_VIEW", "BUSINESS_QUALITY_CHECK_UPDATE",
                "BUSINESS_USER_VIEW", "BUSINESS_USER_CREATE", "BUSINESS_USER_UPDATE",
                "CUSTOMER_DEVICE_VIEW", "CUSTOMER_DEVICE_CREATE", "CUSTOMER_DEVICE_UPDATE",
                "APPOINTMENT_CREATE", "APPOINTMENT_VIEW", "APPOINTMENT_UPDATE",
                "REPAIR_ORDER_CREATE", "REPAIR_ORDER_VIEW", "REPAIR_ORDER_UPDATE",
                "REPAIR_ORDER_FINANCIAL_UPDATE", "REPAIR_ORDER_CLOSE",
                "REPAIR_ORDER_UPLOAD_EVIDENCE",
                "QUOTATION_CREATE", "QUOTATION_VIEW", "QUOTATION_UPDATE",
                "INVENTORY_VIEW", "INVENTORY_COST_VIEW", "INVENTORY_CREATE", "INVENTORY_UPDATE", "INVENTORY_DELETE",
                "SALE_CREATE", "SALE_VIEW", "SALE_COST_VIEW", "REVIEW_VIEW", "REPORT_VIEW",
                "BUSINESS_COMMISSION_VIEW", "BUSINESS_COMMISSION_MANAGE");

        List<String> technician = List.of("CATALOG_VIEW", "BUSINESS_CATALOG_VIEW", "BUSINESS_BRANCH_VIEW",
                "REPAIR_ORDER_VIEW", "REPAIR_ORDER_UPDATE",
                "REPAIR_ORDER_UPLOAD_EVIDENCE", "INVENTORY_VIEW", "QUOTATION_VIEW", "CUSTOMER_DEVICE_VIEW");
        List<String> receptionist = List.of("CATALOG_VIEW", "BUSINESS_CATALOG_VIEW", "BUSINESS_BRANCH_VIEW",
                "APPOINTMENT_CREATE", "APPOINTMENT_VIEW", "APPOINTMENT_UPDATE",
                "REPAIR_ORDER_CREATE", "REPAIR_ORDER_VIEW", "REPAIR_ORDER_FINANCIAL_UPDATE",
                "QUOTATION_CREATE", "QUOTATION_VIEW",
                "CUSTOMER_DEVICE_VIEW", "CUSTOMER_DEVICE_CREATE", "CUSTOMER_DEVICE_UPDATE");
        List<String> seller = List.of("CATALOG_VIEW", "BUSINESS_CATALOG_VIEW", "BUSINESS_BRANCH_VIEW", "INVENTORY_VIEW",
                "SALE_CREATE", "SALE_VIEW");
        List<String> customer = List.of("CATALOG_VIEW", "REVIEW_CREATE", "BUSINESS_REQUEST_CREATE",
                "BUSINESS_REQUEST_VIEW_OWN");

        syncPermissions("PLATFORM_ADMIN", allPermissions);
        syncPermissions("BUSINESS_OWNER", owner);
        syncPermissions("BUSINESS_ADMIN", owner);
        syncPermissions("TECHNICIAN", technician);
        syncPermissions("RECEPTIONIST", receptionist);
        syncPermissions("SELLER", seller);
        syncPermissions("CUSTOMER", customer);
    }

    private void seedParameters() {
        upsertDocumentType("CC", "Cedula de ciudadania", "Documento de identidad colombiano", true, false);
        upsertDocumentType("CE", "Cedula de extranjeria", "Documento para personas extranjeras", true, false);
        upsertDocumentType("PASSPORT", "Pasaporte", "Documento internacional de identidad", true, false);
        upsertDocumentType("NIT", "NIT", "Numero de identificacion tributaria", false, true);

        Country colombia = upsertCountry("CO", "Colombia");
        Department antioquia = upsertDepartment(colombia, "ANT", "Antioquia");
        Department cundinamarca = upsertDepartment(colombia, "CUN", "Cundinamarca");
        Department valleDelCauca = upsertDepartment(colombia, "VAC", "Valle del Cauca");
        Department atlantico = upsertDepartment(colombia, "ATL", "Atlantico");
        Department santander = upsertDepartment(colombia, "SAN", "Santander");

        upsertCity(antioquia, "Medellin");
        upsertCity(cundinamarca, "Bogota");
        upsertCity(valleDelCauca, "Cali");
        upsertCity(atlantico, "Barranquilla");
        upsertCity(santander, "Bucaramanga");
    }

    private void seedPlatformAdmin() {
        validatePlatformAdminConfiguration();
        String normalizedEmail = platformAdminEmail.trim().toLowerCase();
        AppUser admin = appUserRepository.findByEmail(normalizedEmail)
                .orElseGet(() -> createPlatformAdmin(normalizedEmail));
        assignUserRole(admin, "PLATFORM_ADMIN");
    }

    private AppUser createPlatformAdmin(String normalizedEmail) {
        DocumentType documentType = findDocumentType(platformAdminDocumentTypeCode.trim().toUpperCase());
        City city = findCity(platformAdminCity.trim());
        AppUser entity = new AppUser();
        entity.setDocumentType(documentType);
        entity.setDocumentNumber(platformAdminDocumentNumber.trim());
        entity.setName(platformAdminName.trim());
        entity.setSurname(platformAdminSurname.trim());
        entity.setEmail(normalizedEmail);
        entity.setAddress(platformAdminAddress.trim());
        entity.setPhone(platformAdminPhone.trim());
        entity.setCountry(city.getCountry());
        entity.setDepartment(city.getDepartment());
        entity.setCity(city);
        entity.setActive(true);
        entity.setDeletedAt(null);
        entity.setEmailVerified(true);
        entity.setEmailVerifiedAt(LocalDateTime.now());
        entity.setPasswordHash(passwordEncoder.encode(platformAdminPassword));
        return appUserRepository.save(entity);
    }

    private void validatePlatformAdminConfiguration() {
        requireSeedValue(platformAdminEmail, "SEED_PLATFORM_ADMIN_EMAIL");
        requireSeedValue(platformAdminPassword, "SEED_PLATFORM_ADMIN_PASSWORD");
        requireSeedValue(platformAdminDocumentTypeCode, "SEED_PLATFORM_ADMIN_DOCUMENT_TYPE_CODE");
        requireSeedValue(platformAdminDocumentNumber, "SEED_PLATFORM_ADMIN_DOCUMENT_NUMBER");
        requireSeedValue(platformAdminName, "SEED_PLATFORM_ADMIN_NAME");
        requireSeedValue(platformAdminSurname, "SEED_PLATFORM_ADMIN_SURNAME");
        requireSeedValue(platformAdminAddress, "SEED_PLATFORM_ADMIN_ADDRESS");
        requireSeedValue(platformAdminPhone, "SEED_PLATFORM_ADMIN_PHONE");
        requireSeedValue(platformAdminCity, "SEED_PLATFORM_ADMIN_CITY");

        if (!platformAdminEmail.contains("@")) {
            throw new IllegalStateException("SEED_PLATFORM_ADMIN_EMAIL must be a valid email address");
        }
        if (platformAdminPassword.length() < 12) {
            throw new IllegalStateException("SEED_PLATFORM_ADMIN_PASSWORD must contain at least 12 characters");
        }
    }

    private void requireSeedValue(String value, String environmentVariable) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException(environmentVariable + " is required when platform admin seeding is enabled");
        }
    }

    private AppModule upsertModule(String code, String name) {
        AppModule entity = appModuleRepository.findByCode(code).orElseGet(AppModule::new);
        entity.setCode(code);
        entity.setName(name);
        entity.setActive(true);
        entity.setDeletedAt(null);
        return appModuleRepository.save(entity);
    }

    private Permission upsertPermission(String moduleCode, String code, String name, String description) {
        AppModule module = appModuleRepository.findByCode(moduleCode)
                .orElseThrow(() -> new IllegalStateException("Module not found: " + moduleCode));
        Permission entity = permissionRepository.findByCode(code).orElseGet(Permission::new);
        entity.setAppModule(module);
        entity.setCode(code);
        entity.setName(name);
        entity.setDescription(description);
        entity.setScope(GLOBAL_PERMISSION_CODES.contains(code)
                ? PermissionScope.GLOBAL
                : PermissionScope.BUSINESS);
        entity.setActive(true);
        entity.setDeletedAt(null);
        return permissionRepository.save(entity);
    }

    private Role upsertRole(String code, String name, String description, String scope) {
        Role entity = roleRepository.findByCode(code).orElseGet(Role::new);
        entity.setCode(code);
        entity.setName(name);
        entity.setDescription(description);
        entity.setScope(scope);
        entity.setActive(true);
        entity.setDeletedAt(null);
        return roleRepository.save(entity);
    }

    private void syncPermissions(String roleCode, List<String> permissionCodes) {
        Role role = roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleCode));
        Set<String> desiredCodes = new LinkedHashSet<>(permissionCodes);
        List<RolePermission> currentRelations = rolePermissionRepository.findByRoleId(role.getId());
        Map<String, RolePermission> relationByCode = currentRelations.stream()
                .collect(Collectors.toMap(
                        relation -> relation.getPermission().getCode(),
                        relation -> relation,
                        (first, second) -> first));
        LocalDateTime now = LocalDateTime.now();

        for (RolePermission relation : currentRelations) {
            if (desiredCodes.contains(relation.getPermission().getCode())) {
                relation.setActive(true);
                relation.setDeletedAt(null);
            } else {
                relation.setActive(false);
                relation.setDeletedAt(now);
            }
        }

        for (String permissionCode : desiredCodes) {
            if (relationByCode.containsKey(permissionCode)) {
                continue;
            }
            Permission permission = permissionRepository.findByCode(permissionCode)
                    .orElseThrow(() -> new IllegalStateException("Permission not found: " + permissionCode));
            RolePermission created = new RolePermission();
            created.setRole(role);
            created.setPermission(permission);
            created.setActive(true);
            currentRelations.add(created);
        }

        rolePermissionRepository.saveAll(currentRelations);
    }

    private DocumentType upsertDocumentType(String code, String name, String description, boolean person,
            boolean company) {
        DocumentType entity = documentTypeRepository.findByCode(code).orElseGet(DocumentType::new);
        entity.setCode(code);
        entity.setName(name);
        entity.setDescription(description);
        entity.setAppliesToPerson(person);
        entity.setAppliesToCompany(company);
        entity.setActive(true);
        entity.setDeletedAt(null);
        return documentTypeRepository.save(entity);
    }

    private Country upsertCountry(String code, String name) {
        Country entity = countryRepository.findByCode(code).orElseGet(Country::new);
        entity.setCode(code);
        entity.setName(name);
        entity.setActive(true);
        entity.setDeletedAt(null);
        return countryRepository.save(entity);
    }

    private Department upsertDepartment(Country country, String code, String name) {
        Department entity = departmentRepository.findByCountryIdAndCodeIgnoreCase(country.getId(), code)
                .orElseGet(Department::new);
        entity.setCountry(country);
        entity.setCode(code);
        entity.setName(name);
        entity.setActive(true);
        entity.setDeletedAt(null);
        return departmentRepository.save(entity);
    }

    private City upsertCity(Department department, String name) {
        City entity = cityRepository.findByDepartmentIdAndDeletedAtIsNullOrderByNameAsc(department.getId())
                .stream()
                .filter(city -> city.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(City::new);
        entity.setCountry(department.getCountry());
        entity.setDepartment(department);
        entity.setName(name);
        entity.setActive(true);
        entity.setDeletedAt(null);
        return cityRepository.save(entity);
    }

    private void assignUserRole(AppUser user, String roleCode) {
        Role role = roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleCode));
        UserRole relation = userRoleRepository.findByUserIdAndRoleCode(user.getId(), roleCode).orElseGet(UserRole::new);
        relation.setUser(user);
        relation.setRole(role);
        relation.setActive(true);
        relation.setDeletedAt(null);
        userRoleRepository.save(relation);
    }

    private DocumentType findDocumentType(String code) {
        return documentTypeRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Document type not found: " + code));
    }

    private City findCity(String name) {
        return cityRepository.findAll().stream()
                .filter(city -> city.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("City not found: " + name));
    }
}
