package com.lingoor.backend.config;

import com.lingoor.backend.constants.Constants;
import com.lingoor.backend.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        UsernamePasswordAuthenticationProvider authenticationProvider = new UsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder);

        ProviderManager providerManager = new ProviderManager(authenticationProvider);

        // Prevent credentials (e.g., passwords) from being erased after authentication
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cc -> cc.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList(Constants.FRONTEND_APP_URL)); // Allow requests only from this origin (Angular frontend)
                    config.setAllowedMethods(Collections.singletonList("*")); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
                    config.setAllowCredentials(true); // Allow credentials (cookies, Authorization headers, etc.)
                    config.setAllowedHeaders(Collections.singletonList("*")); // Allow all headers in the request
                    config.setExposedHeaders(List.of(Constants.AUTH_HEADER)); // Expose Authorization header to the client
                    config.setMaxAge(3600L); // Cache the CORS configuration for 1 hour
                    return config;
                }))
                //allow login, register and community feed
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/register", "/api/login").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/posts").permitAll()                           // ONLY the community feed
                        .requestMatchers(HttpMethod.GET,  "/api/posts/*/likes/count").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}