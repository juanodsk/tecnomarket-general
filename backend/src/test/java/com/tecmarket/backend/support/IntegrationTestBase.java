package com.tecmarket.backend.support;

import com.sun.net.httpserver.HttpServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class IntegrationTestBase {

    private static final HttpServer RESEND_SERVER;
    private static final String RESEND_BASE_URL;

    static {
        try {
            RESEND_SERVER = HttpServer.create(new InetSocketAddress(0), 0);
            RESEND_SERVER.createContext("/emails", exchange -> {
                byte[] body = "{\"id\":\"email_test_ok\"}".getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, body.length);
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(body);
                }
            });
            RESEND_SERVER.start();
            RESEND_BASE_URL = "http://localhost:" + RESEND_SERVER.getAddress().getPort();
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("app.notification.resend.base-url", () -> RESEND_BASE_URL);
    }

}
