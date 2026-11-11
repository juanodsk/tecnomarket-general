/*
 * TecMarket Backend
 * Module: Auth
 */

package com.tecmarket.backend.auth.controller;

import com.tecmarket.backend.auth.dto.AuthUserResponse;
import com.tecmarket.backend.auth.dto.AuthUserPermissionsResponse;
import com.tecmarket.backend.auth.dto.LoginRequest;
import com.tecmarket.backend.auth.dto.LoginResponse;
import com.tecmarket.backend.auth.dto.RegisterRequest;
import com.tecmarket.backend.auth.dto.VerifyEmailRequest;
import com.tecmarket.backend.auth.dto.AuthSessionResponse;
import com.tecmarket.backend.auth.dto.RequestPasswordResetRequest;
import com.tecmarket.backend.auth.dto.ResetPasswordRequest;
import com.tecmarket.backend.auth.dto.ChangePasswordRequest;
import com.tecmarket.backend.auth.service.PasswordResetService;
import com.tecmarket.backend.auth.service.AuthService;
import com.tecmarket.backend.common.i18n.MessageService;
import com.tecmarket.backend.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Authentication", description = "Endpoints for user registration and login.")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final MessageService messageService;
    private final PasswordResetService passwordResetService;
    private final boolean refreshCookieSecure;
    private final String refreshCookieSameSite;
    private final String refreshCookieDomain;
    private final long refreshTokenExpirationDays;

    public AuthController(
            AuthService authService,
            MessageService messageService,
            PasswordResetService passwordResetService,
            @Value("${app.security.refresh-cookie.secure:true}") boolean refreshCookieSecure,
            @Value("${app.security.refresh-cookie.same-site:Lax}") String refreshCookieSameSite,
            @Value("${app.security.refresh-cookie.domain:}") String refreshCookieDomain,
            @Value("${app.security.jwt.refresh-token-expiration-days:7}") long refreshTokenExpirationDays) {
        this.authService = authService;
        this.messageService = messageService;
        this.passwordResetService = passwordResetService;
        this.refreshCookieSecure = refreshCookieSecure;
        this.refreshCookieSameSite = refreshCookieSameSite;
        this.refreshCookieDomain = refreshCookieDomain;
        this.refreshTokenExpirationDays = refreshTokenExpirationDays;
    }

    @Operation(summary = "Register user", description = "Creates a global user account in TecMarket.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthUserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthUserResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(messageService.get("auth.register.success"), response));
    }

    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT access token.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        LoginResponse response = authService.login(
                request,
                servletRequest.getHeader(HttpHeaders.USER_AGENT),
                servletRequest.getRemoteAddr());
        setRefreshCookie(servletResponse, response.refreshToken());

        return ResponseEntity
                .ok(ApiResponse.success(messageService.get("auth.login.success"), response));
    }

    @Operation(summary = "Verify email", description = "Confirms user email using a verification token.")
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request.token());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.verify.success"), null));
    }

    @Operation(summary = "Request password reset", description = "Always returns the same response to prevent account enumeration.")
    @PostMapping("/password-reset/request")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(
            @Valid @RequestBody RequestPasswordResetRequest request) {
        passwordResetService.request(request.email());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.password_reset.request.success"), null));
    }

    @Operation(summary = "Reset password", description = "Consumes a one-time password reset token and revokes all active sessions.")
    @PostMapping("/password-reset/confirm")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.reset(request.token(), request.newPassword());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.password_reset.success"), null));
    }

    @Operation(summary = "Change password", description = "Changes the current user's password and invalidates every access and refresh token.", security = {
            @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/password/change")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletResponse servletResponse) {
        authService.changePassword(jwt.getSubject(), request.currentPassword(), request.newPassword());
        clearRefreshCookie(servletResponse);
        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.password.change.success"), null));
    }

    @Operation(summary = "Get authenticated user", description = "Returns the profile of the authenticated user.", security = {
            @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserResponse>> me(@AuthenticationPrincipal Jwt jwt) {
        AuthUserResponse response = authService.getAuthenticatedUser(jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.me.success"), response));
    }

    @Operation(summary = "Get authenticated user permissions", description = "Returns active roles and permissions of the authenticated user.", security = {
            @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/me/permissions")
    public ResponseEntity<ApiResponse<AuthUserPermissionsResponse>> mePermissions(@AuthenticationPrincipal Jwt jwt) {
        AuthUserPermissionsResponse response = authService.getAuthenticatedUserPermissions(jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.me.permissions.success"), response));
    }

    @Operation(summary = "Refresh access token", description = "Generates a new access token using a refresh token.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue(name = "tm_refresh") String refreshToken,
            HttpServletResponse servletResponse) {
        LoginResponse response = authService.refreshAccessToken(refreshToken);
        setRefreshCookie(servletResponse, response.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.refresh.success"), response));
    }

    @Operation(summary = "Logout user", description = "Revokes the refresh token and clears the security context.", security = {
            @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "tm_refresh") String refreshToken,
            HttpServletResponse servletResponse,
            @AuthenticationPrincipal Jwt jwt) {
        authService.logout(refreshToken, jwt.getSubject());
        clearRefreshCookie(servletResponse);

        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.logout.success"), null));
    }

    @Operation(summary = "List active sessions", description = "Returns the active refresh-token sessions for the current user.")
    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<List<AuthSessionResponse>>> sessions(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ApiResponse.success(
                messageService.get("auth.sessions.list.success"),
                authService.findSessions(jwt.getSubject())));
    }

    @Operation(summary = "Revoke one session", description = "Revokes one refresh-token family owned by the current user.")
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> revokeSession(
            @PathVariable UUID sessionId,
            @AuthenticationPrincipal Jwt jwt) {
        authService.revokeSession(sessionId, jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.sessions.revoke.success"), null));
    }

    @Operation(summary = "Revoke all sessions", description = "Revokes every session owned by the current user, including the current access token.")
    @DeleteMapping("/sessions")
    public ResponseEntity<ApiResponse<Void>> revokeAllSessions(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletResponse servletResponse) {
        authService.revokeAllSessions(jwt.getSubject());
        clearRefreshCookie(servletResponse);
        return ResponseEntity.ok(ApiResponse.success(messageService.get("auth.sessions.revoke_all.success"), null));
    }

    private void setRefreshCookie(HttpServletResponse response, String refreshToken) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder(refreshToken)
                .maxAge(Duration.ofDays(Math.max(1, refreshTokenExpirationDays)))
                .build()
                .toString());
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder("")
                .maxAge(Duration.ZERO)
                .build()
                .toString());
    }

    private ResponseCookie.ResponseCookieBuilder cookieBuilder(String value) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("tm_refresh", value)
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .sameSite(refreshCookieSameSite)
                .path("/api/v1/auth");
        if (StringUtils.hasText(refreshCookieDomain)) {
            builder.domain(refreshCookieDomain.trim());
        }
        return builder;
    }

}
