package com.example.LocalGPT_Dshboard.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiKeyFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    @Value("${api.key}")
    private String validApiKey;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Only protect /api/chat, leave /api/health open
        String path = request.getRequestURI();
        if (path.equals("/api/chat")) {
            String apiKey = request.getHeader("X-API-KEY");
            if (apiKey == null || !apiKey.equals(validApiKey)) {
                log.warn("[{}] Rejected request - invalid API key from {}",
                        java.time.LocalDateTime.now(), request.getRemoteAddr());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid or missing X-API-KEY header\"}");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}