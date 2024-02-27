package com.popfilms.popfilmsapi.Security;

import com.popfilms.popfilmsapi.Security.Authentication.CustomTokenAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.HashSet;

@Component
public class CustomTokenFilter extends OncePerRequestFilter {

    @Autowired
    private CustomAuthenticationProviderManager customAuthenticationProviderManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie authCookie = WebUtils.getCookie(request, "popfilms_auth");
        if (authCookie != null){
            String authToken = authCookie.getValue();
            Authentication authentication;
            CustomTokenAuthentication tokenAuthentication = new CustomTokenAuthentication(false, authToken, new HashSet<>());
            authentication = customAuthenticationProviderManager.authenticate(tokenAuthentication);
            if (authentication.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
