package com.tecmarket.backend.config;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

final class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private static final int MAX_AUTH_BODY_BYTES = 16 * 1024;
    private final byte[] cachedBody;
    private final boolean tooLarge;

    CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        byte[] body = request.getInputStream().readNBytes(MAX_AUTH_BODY_BYTES + 1);
        this.tooLarge = body.length > MAX_AUTH_BODY_BYTES;
        this.cachedBody = body;
    }

    byte[] getCachedBody() {
        return cachedBody.clone();
    }

    boolean isTooLarge() {
        return tooLarge;
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream input = new ByteArrayInputStream(cachedBody);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return input.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Synchronous request processing does not require a listener.
            }

            @Override
            public int read() {
                return input.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        Charset charset = getCharacterEncoding() == null
                ? StandardCharsets.UTF_8
                : Charset.forName(getCharacterEncoding());
        return new BufferedReader(new InputStreamReader(getInputStream(), charset));
    }
}
