package com.popfilms.popfilmsapi.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class Custom401AuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseCookie cookie = ResponseCookie.from("popfilms_auth", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String jsonResponse = String.format("{\"timestamp\":\"%s\",\"status\":401,\"error\":\"Unauthorized\",\"path\":\"%s\"}",
                Instant.now().toString(), request.getRequestURI());

        // Set the JSON response as the body
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
