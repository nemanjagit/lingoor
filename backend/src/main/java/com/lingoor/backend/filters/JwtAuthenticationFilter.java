package com.lingoor.backend.filters;

import com.lingoor.backend.constants.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException, ServletException {
        // Extract the JWT from the Authorization header
        String jwt = request.getHeader(SecurityConstants.AUTH_HEADER);

        // Proceed only if a token is present
        if (jwt != null) {
            try {
                // Load the JWT secret key from the environment
                Environment env = getEnvironment();
                String secret = env.getProperty(SecurityConstants.JWT_SECRET_KEY, SecurityConstants.JWT_SECRET_KEY_DEFAULT_VALUE);

                // Create the signing key using the secret
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                // Parse and validate the JWT, extract the claims payload
                Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();

                // Extract the identifier (username) from the claims
                String identifier = String.valueOf(claims.get(SecurityConstants.CLAIM_IDENTIFIER));

                // Create an authentication token with the extracted identifier
                // No credentials or authorities are set at this point
                Authentication authentication = new UsernamePasswordAuthenticationToken(identifier, null, AuthorityUtils.NO_AUTHORITIES);

                // Store the authentication object in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception exception) {
                // If the token is invalid or expired, throw an exception
                throw new BadCredentialsException(SecurityConstants.INVALID_JWT_TOKEN);
            }
        }

        // Continue the filter chain regardless of JWT presence
        filterChain.doFilter(request, response);
    }

}