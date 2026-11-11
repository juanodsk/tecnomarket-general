package com.tecmarket.backend.common.pagination;

import com.tecmarket.backend.common.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PageRequestFactoryTest {

    @Test
    void appliesStandardDefaultsAndMaximum() {
        assertThat(PageRequestFactory.of(null, null).getPageNumber()).isZero();
        assertThat(PageRequestFactory.of(null, null).getPageSize()).isEqualTo(20);
        assertThat(PageRequestFactory.of(2, 100).getPageSize()).isEqualTo(100);
    }

    @Test
    void rejectsInvalidPageOrSizeInsteadOfSilentlyClamping() {
        assertThatThrownBy(() -> PageRequestFactory.of(-1, 20))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("pagination.invalid");
        assertThatThrownBy(() -> PageRequestFactory.of(0, 0))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> PageRequestFactory.of(0, 101))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void mapsSpringPageToStableApiEnvelope() {
        var source = new PageImpl<>(List.of("a", "b"), PageRequest.of(1, 2), 5);

        PageResponse<String> response = PageResponse.from(source);

        assertThat(response.items()).containsExactly("a", "b");
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.totalElements()).isEqualTo(5);
        assertThat(response.totalPages()).isEqualTo(3);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.hasPrevious()).isTrue();
    }
}
