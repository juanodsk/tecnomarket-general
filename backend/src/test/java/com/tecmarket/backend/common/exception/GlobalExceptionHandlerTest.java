package com.tecmarket.backend.common.exception;

import com.tecmarket.backend.common.i18n.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import jakarta.servlet.http.HttpServletRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        MessageService messageService = mock(MessageService.class);
        when(messageService.get("data.integrity.conflict")).thenReturn("conflict");
        when(messageService.get("validation.request.invalid")).thenReturn("invalid");
        when(messageService.get("business_service.not_found"))
                .thenReturn("Business service was not found.");
        when(messageService.get("resource.not_found"))
                .thenReturn("The requested resource does not exist.");
        handler = new GlobalExceptionHandler(messageService);
    }

    @Test
    void mapsDatabaseConstraintViolationsToConflictWithoutReturningDatabaseDetails() {
        var response = handler.handleDataIntegrity(
                new DataIntegrityViolationException("duplicate email user@example.com"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().success()).isFalse();
        assertThat(response.getBody().message()).isEqualTo("conflict");
        assertThat(response.getBody().data()).isNull();
    }

    @Test
    void logsInternalResourceCodeAndKeepsLocalizedNotFoundResponse() {
        var response = handler.handleResourceNotFound(
                new ResourceNotFoundException("business_service.not_found"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().success()).isFalse();
        assertThat(response.getBody().message()).isEqualTo("Business service was not found.");
        assertThat(response.getBody().data()).isNull();
    }

    @Test
    void logsUnmappedRouteAndKeepsGenericNotFoundResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/v1/businesses/1/repair-orders/quick");

        var response = handler.handleNoResourceFound(
                new NoResourceFoundException(HttpMethod.POST, "/api/v1/businesses/1/repair-orders/quick"),
                request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().success()).isFalse();
        assertThat(response.getBody().message()).isEqualTo("The requested resource does not exist.");
        assertThat(response.getBody().data()).isNull();
    }
}
