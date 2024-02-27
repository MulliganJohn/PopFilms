package com.popfilms.popfilmsapi.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig{

    @Autowired
    CustomTokenFilter customTokenFilter;

    @Autowired
    Custom401AuthenticationEntryPoint custom401AuthenticationEntryPoint;

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("api/auth/**", "/Error", "api/movies/**", "api/reviews/**", "api/users/**")
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().permitAll();
                })
                .cors(withDefaults())
                .csrf().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain tokenSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/reviews/addreview", "/api/reviews/editreview", "/api/reviews/removereview", "/api/users/signout", "/api/users/signoutall")
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().hasRole("USER").and().addFilterBefore(customTokenFilter, UsernamePasswordAuthenticationFilter.class);
                })
                .csrf().disable()
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(custom401AuthenticationEntryPoint))
                .build();
    }

    @Bean
    @Order(0)
    public SecurityFilterChain addMovieRoleFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/movies/addmovie")
                .authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().hasAnyRole("MOD", "ADMIN").and().addFilterBefore(customTokenFilter, UsernamePasswordAuthenticationFilter.class);
                })
                .csrf().disable()
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(custom401AuthenticationEntryPoint))
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean registration(CustomTokenFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

}
