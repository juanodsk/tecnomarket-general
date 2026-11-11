package com.tecmarket.backend.support;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostgresFlywayMigrationTest {

    private static PostgreSQLContainer<?> postgres;
    private static String jdbcUrl;
    private static String username;
    private static String password;

    @BeforeAll
    static void configurePostgres() {
        String externalJdbcUrl = System.getenv("TEST_POSTGRES_JDBC_URL");
        if (externalJdbcUrl != null && !externalJdbcUrl.isBlank()) {
            jdbcUrl = externalJdbcUrl;
            username = System.getenv().getOrDefault("TEST_POSTGRES_USERNAME", "tecmarket_test");
            password = System.getenv().getOrDefault("TEST_POSTGRES_PASSWORD", "");
            return;
        }

        assumeTrue(DockerClientFactory.instance().isDockerAvailable(),
                "Docker or TEST_POSTGRES_JDBC_URL is required for the PostgreSQL migration gate.");
        postgres = new PostgreSQLContainer<>("postgres:17-alpine")
                .withDatabaseName("tecmarket_migration_test")
                .withUsername("tecmarket_test")
                .withPassword("tecmarket_test");
        postgres.start();
        jdbcUrl = postgres.getJdbcUrl();
        username = postgres.getUsername();
        password = postgres.getPassword();
    }

    @AfterAll
    static void stopPostgres() {
        if (postgres != null) {
            postgres.stop();
        }
    }

    @Test
    @Order(1)
    void migratesAnExistingV38DatabaseToLatestOnRealPostgres() {
        Flyway v38 = flyway("38");
        v38.migrate();
        assertThat(v38.info().current().getVersion().getVersion()).isEqualTo("38");

        Flyway latest = flyway(null);
        latest.migrate();

        assertThat(latest.info().pending()).isEmpty();
        assertThat(latest.info().current().getVersion().getVersion()).isEqualTo("49");
        assertStorageLifecycleSchema();
        assertOperationalFlowSchema();
        assertDatabaseContractSchema();
    }

    @Test
    @Order(2)
    void concurrentStockConsumptionSerializesAndNeverProducesNegativeStock() throws Exception {
        int inventoryItemId = createInventoryFixture(10);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try (var executor = Executors.newFixedThreadPool(2)) {
            var first = executor.submit(() -> consumeStock(inventoryItemId, 7, ready, start));
            var second = executor.submit(() -> consumeStock(inventoryItemId, 7, ready, start));

            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            long successfulConsumptions = Stream.of(first, second)
                    .map(future -> {
                        try {
                            return future.get(10, TimeUnit.SECONDS);
                        } catch (Exception exception) {
                            throw new AssertionError("Concurrent stock transaction did not finish", exception);
                        }
                    })
                    .filter(Boolean::booleanValue)
                    .count();

            assertThat(successfulConsumptions).isEqualTo(1);
        }

        try (var connection = DriverManager.getConnection(jdbcUrl, username, password);
                var statement = connection.prepareStatement(
                        "SELECT quantity FROM inventory_item WHERE id = ?")) {
            statement.setInt(1, inventoryItemId);
            try (var result = statement.executeQuery()) {
                assertThat(result.next()).isTrue();
                assertThat(result.getInt(1)).isEqualTo(3);
            }
        }
    }

    private int createInventoryFixture(int quantity) throws SQLException {
        String fixtureSql = """
                WITH document AS (
                    INSERT INTO document_type (
                        code, name, description, applies_to_person, applies_to_company, active)
                    VALUES ('QA_NIT', 'QA NIT', 'QA document', FALSE, TRUE, TRUE)
                    RETURNING id
                ), country_row AS (
                    INSERT INTO country (code, name, active)
                    VALUES ('QA', 'QA Country', TRUE)
                    RETURNING id
                ), department_row AS (
                    INSERT INTO department (country_id, code, name, active)
                    SELECT id, 'QA', 'QA Department', TRUE FROM country_row
                    RETURNING id, country_id
                ), city_row AS (
                    INSERT INTO city (country_id, department_id, name, active)
                    SELECT country_id, id, 'QA City', TRUE FROM department_row
                    RETURNING id
                ), business_row AS (
                    INSERT INTO business (
                        document_type_id, document_number, name, legal_name, email, phone,
                        address, description, city_id, rating_average, rating_count, active)
                    SELECT document.id, 'QA-001', 'QA Business', 'QA Business SAS',
                           'qa-business@example.test', '0000000', 'QA address', 'QA fixture',
                           city_row.id, 0, 0, TRUE
                    FROM document, city_row
                    RETURNING id, city_id
                ), branch_row AS (
                    INSERT INTO business_branch (
                        business_id, name, city_id, address, branch_number, primary_branch, active)
                    SELECT id, 'QA Branch', city_id, 'QA address', 1, TRUE, TRUE
                    FROM business_row
                    RETURNING id, business_id
                ), category_row AS (
                    INSERT INTO business_catalog_item (
                        business_id, catalog_type, code, name, protected_item, active)
                    SELECT business_id, 'ITEM_CATEGORY', 'SPARE_PART', 'QA Spare Part', FALSE, TRUE
                    FROM branch_row
                    RETURNING id, business_id
                ), inventory_row AS (
                    INSERT INTO inventory_item (
                        business_id, branch_id, name, item_category_id, sku, quantity,
                        minimum_stock, unit_cost, sale_price, active)
                    SELECT category_row.business_id, branch_row.id, 'QA Battery', category_row.id,
                           'QA-BATTERY-001', ?, 0, 10.00, 20.00, TRUE
                    FROM category_row, branch_row
                    RETURNING id
                )
                SELECT id FROM inventory_row
                """;

        try (var connection = DriverManager.getConnection(jdbcUrl, username, password);
                var statement = connection.prepareStatement(fixtureSql)) {
            statement.setInt(1, quantity);
            try (var result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new SQLException("Inventory fixture was not created");
                }
                return result.getInt(1);
            }
        }
    }

    private boolean consumeStock(
            int inventoryItemId,
            int requestedQuantity,
            CountDownLatch ready,
            CountDownLatch start) throws Exception {
        ready.countDown();
        if (!start.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Concurrent stock test did not start");
        }

        try (var connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            connection.setAutoCommit(false);
            try {
                int currentQuantity;
                try (var lock = connection.prepareStatement(
                        "SELECT quantity FROM inventory_item WHERE id = ? FOR UPDATE")) {
                    lock.setInt(1, inventoryItemId);
                    try (var result = lock.executeQuery()) {
                        if (!result.next()) {
                            throw new SQLException("Inventory item disappeared");
                        }
                        currentQuantity = result.getInt(1);
                    }
                }

                if (currentQuantity < requestedQuantity) {
                    connection.rollback();
                    return false;
                }

                try (var update = connection.prepareStatement(
                        "UPDATE inventory_item SET quantity = quantity - ? WHERE id = ?")) {
                    update.setInt(1, requestedQuantity);
                    update.setInt(2, inventoryItemId);
                    assertThat(update.executeUpdate()).isEqualTo(1);
                }
                connection.commit();
                return true;
            } catch (Exception exception) {
                connection.rollback();
                throw exception;
            }
        }
    }

    private void assertDatabaseContractSchema() {
        try (var connection = DriverManager.getConnection(jdbcUrl, username, password);
                var statement = connection.createStatement()) {
            try (var result = statement.executeQuery("""
                    SELECT COUNT(*)
                    FROM pg_constraint
                    WHERE conname IN (
                        'fk_quotation_order_business_customer',
                        'fk_business_review_order_business_customer',
                        'fk_repair_order_commission_order_business',
                        'fk_repair_order_commission_user_business',
                        'fk_commission_settlement_user_business',
                        'fk_repair_order_commission_settlement_business',
                        'fk_stock_movement_order_branch',
                        'chk_repair_order_part_total_consistent',
                        'chk_quotation_item_total_consistent',
                        'chk_sale_item_totals_consistent')
                    """)) {
                result.next();
                assertThat(result.getInt(1)).isEqualTo(10);
            }
            try (var result = statement.executeQuery("""
                    SELECT COUNT(*)
                    FROM pg_indexes
                    WHERE schemaname = 'public'
                      AND indexname IN (
                        'idx_business_request_requester_created',
                        'idx_business_request_pending_created',
                        'idx_business_user_business_id',
                        'idx_customer_device_business_registered')
                    """)) {
                result.next();
                assertThat(result.getInt(1)).isEqualTo(4);
            }
        } catch (Exception exception) {
            throw new AssertionError("Could not verify V49 database contract schema", exception);
        }
    }

    private void assertOperationalFlowSchema() {
        try (var connection = DriverManager.getConnection(jdbcUrl, username, password);
                var statement = connection.createStatement()) {
            try (var result = statement.executeQuery("""
                    SELECT COUNT(*)
                    FROM information_schema.columns
                    WHERE table_schema = 'public'
                      AND table_name = 'repair_order_part'
                      AND column_name IN ('idempotency_key', 'idempotency_fingerprint')
                    """)) {
                result.next();
                assertThat(result.getInt(1)).isEqualTo(2);
            }
            try (var result = statement.executeQuery("""
                    SELECT COUNT(*) FROM pg_indexes
                    WHERE schemaname = 'public'
                      AND indexname = 'uq_repair_order_part_order_idempotency'
                      AND indexdef LIKE '%WHERE%'
                    """)) {
                result.next();
                assertThat(result.getInt(1)).isEqualTo(1);
            }
            try (var result = statement.executeQuery("""
                    SELECT COUNT(*) FROM pg_trigger
                    WHERE tgname = 'trg_repair_order_history_immutable'
                      AND NOT tgisinternal
                    """)) {
                result.next();
                assertThat(result.getInt(1)).isEqualTo(1);
            }
        } catch (Exception exception) {
            throw new AssertionError("Could not verify V48 operational flow schema", exception);
        }
    }

    private void assertStorageLifecycleSchema() {
        try (var connection = DriverManager.getConnection(jdbcUrl, username, password);
                var statement = connection.createStatement()) {
            try (var result = statement.executeQuery("""
                    SELECT COUNT(*)
                    FROM information_schema.columns
                    WHERE table_schema = 'public'
                      AND ((table_name = 'repair_order_attachment'
                            AND column_name IN ('checksum_sha256', 'deleted_by_business_user_id', 'retention_until'))
                        OR (table_name = 'storage_object_deletion_task'
                            AND column_name IN ('bucket_name', 'object_key', 'scheduled_at', 'status', 'attempt_count')))
                    """)) {
                result.next();
                assertThat(result.getInt(1)).isEqualTo(8);
            }
            try (var result = statement.executeQuery("""
                    SELECT COUNT(*) FROM pg_indexes
                    WHERE schemaname = 'public'
                      AND indexname = 'uq_storage_deletion_pending_object'
                      AND indexdef LIKE '%WHERE%'
                      AND indexdef LIKE '%PENDING%'
                    """)) {
                result.next();
                assertThat(result.getInt(1)).isEqualTo(1);
            }
            try (var result = statement.executeQuery("""
                    SELECT COUNT(*) FROM pg_trigger
                    WHERE tgname = 'trg_repair_order_attachment_content_immutable'
                      AND NOT tgisinternal
                    """)) {
                result.next();
                assertThat(result.getInt(1)).isEqualTo(1);
            }
        } catch (Exception exception) {
            throw new AssertionError("Could not verify V47 storage lifecycle schema", exception);
        }
    }

    private Flyway flyway(String target) {
        var configuration = Flyway.configure()
                .dataSource(jdbcUrl, username, password)
                .locations("classpath:db/migration")
                .validateOnMigrate(true)
                .cleanDisabled(true);
        if (target != null) {
            configuration.target(target);
        }
        return configuration.load();
    }
}
