/*
 * TecMarket Backend
 * Module: Config
 */

package com.tecmarket.backend.config;

import com.tecmarket.backend.auth.security.RestAccessDeniedHandler;
import com.tecmarket.backend.auth.security.RestAuthenticationEntryPoint;
import com.tecmarket.backend.auth.security.JwtSecurityVersionValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private static final MacAlgorithm SIGNATURE_ALGORITHM = MacAlgorithm.HS256;
        private static final int MIN_HS256_SECRET_BYTES = 32;

        @Bean
        @Order(1)
        public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http, RateLimitFilter rateLimitFilter)
                        throws Exception {
                return http
                                .securityMatcher(
                                                 "/api/v1/auth/register",
                                                 "/api/v1/auth/login",
                                                 "/api/v1/auth/refresh",
                                                 "/api/v1/auth/verify-email",
                                                 "/api/v1/auth/password-reset/**",
                                                "/actuator/health",
                                                "/actuator/health/liveness",
                                                "/actuator/health/readiness",
                                                "/error",
                                                "/v3/api-docs/**",
                                                "/swagger-ui/**",
                                                "/swagger-ui.html",
                                                "/webjars/**")
                                .csrf(AbstractHttpConfigurer::disable)
                                .addFilterBefore(rateLimitFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                                .cors(Customizer.withDefaults())
                                .formLogin(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .logout(AbstractHttpConfigurer::disable)
                                .headers(this::configureSecurityHeaders)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .anyRequest().permitAll())
                                .build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain apiSecurityFilterChain(
                        HttpSecurity http,
                        JwtDecoder jwtDecoder,
                        JwtAuthenticationConverter jwtAuthenticationConverter,
                        RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                        RestAccessDeniedHandler restAccessDeniedHandler,
                        RateLimitFilter rateLimitFilter) throws Exception {
                return http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(Customizer.withDefaults())
                                .formLogin(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .logout(AbstractHttpConfigurer::disable)
                                .headers(this::configureSecurityHeaders)
                                .addFilterBefore(rateLimitFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(restAuthenticationEntryPoint)
                                                .accessDeniedHandler(restAccessDeniedHandler))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/parameters/document-types")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/public/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt
                                                                .decoder(jwtDecoder)
                                                                .jwtAuthenticationConverter(
                                                                                jwtAuthenticationConverter)))
                                .build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource(
                        @Value("${app.security.cors.allowed-origins:http://localhost:3000}") String allowedOrigins) {
                List<String> origins = Arrays.stream(allowedOrigins.split(","))
                                .map(String::trim)
                                .filter(StringUtils::hasText)
                                .toList();

                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(origins);
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setExposedHeaders(List.of(
                                "Authorization",
                                CorrelationIdFilter.HEADER,
                                "Retry-After",
                                "ETag"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public JwtDecoder jwtDecoder(
                        @Value("${app.security.jwt.secret:}") String jwtSecret,
                        @Value("${app.security.jwt.issuer:tecmarket}") String issuer,
                        @Value("${app.security.jwt.audience:tecmarket-api}") String audience,
                        JwtSecurityVersionValidator securityVersionValidator) {
                NimbusJwtDecoder decoder = NimbusJwtDecoder
                                .withSecretKey(signingKey(jwtSecret))
                                .macAlgorithm(SIGNATURE_ALGORITHM)
                                .build();
                decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                                JwtValidators.createDefaultWithIssuer(issuer),
                                new JwtClaimValidator<List<String>>("aud", values -> values != null && values.contains(audience)),
                                securityVersionValidator));
                return decoder;
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {
                JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
                authoritiesConverter.setAuthoritiesClaimName("authorities");
                authoritiesConverter.setAuthorityPrefix("");

                JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
                authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

                return authenticationConverter;
        }

        private SecretKey signingKey(String jwtSecret) {
                String secret = jwtSecret == null ? "" : jwtSecret.trim();
                byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);

                if (secretBytes.length < MIN_HS256_SECRET_BYTES) {
                        throw new BadJwtException("JWT secret must have at least 32 bytes for HS256.");
                }

                return new SecretKeySpec(secretBytes, SIGNATURE_ALGORITHM.getName());
        }

        private void configureSecurityHeaders(
                        org.springframework.security.config.annotation.web.configurers.HeadersConfigurer<HttpSecurity> headers) {
                headers
                                .contentTypeOptions(Customizer.withDefaults())
                                .frameOptions(frame -> frame.deny())
                                .httpStrictTransportSecurity(hsts -> hsts
                                                .includeSubDomains(true)
                                                .preload(true)
                                                .maxAgeInSeconds(31536000))
                                .referrerPolicy(referrer -> referrer
                                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                                .permissionsPolicyHeader(policy -> policy
                                                .policy("camera=(), geolocation=(), microphone=(), payment=(), usb=()"))
                                .contentSecurityPolicy(csp -> csp.policyDirectives(
                                                "default-src 'self'; object-src 'none'; base-uri 'self'; "
                                                                + "frame-ancestors 'none'; form-action 'self'; "
                                                                + "script-src 'self'; style-src 'self' 'unsafe-inline'; "
                                                                + "img-src 'self' data:"));
        }
}
