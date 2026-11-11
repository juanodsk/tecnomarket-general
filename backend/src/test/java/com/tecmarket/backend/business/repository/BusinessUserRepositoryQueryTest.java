package com.tecmarket.backend.business.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Query;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessUserRepositoryQueryTest {

    @Test
    void technicianByBranchQueryAvoidsPostgresDistinctOrderByConflict() throws Exception {
        Query query = BusinessUserRepository.class
                .getMethod(
                        "findActiveByBusinessBranchAndRoleCode",
                        Integer.class,
                        Integer.class,
                        String.class)
                .getAnnotation(Query.class);

        assertThat(query).isNotNull();
        assertThat(query.value())
                .contains("SELECT bu")
                .doesNotContain("SELECT DISTINCT bu")
                .contains("ORDER BY u.name ASC, u.surname ASC, bu.id ASC");
    }
}
