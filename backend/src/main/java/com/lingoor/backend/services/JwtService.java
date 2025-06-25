package com.lingoor.backend.services;

import com.lingoor.backend.constants.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final Environment env;

    public String generateToken(String email) {
        String jwtSecret = env.getProperty(Constants.JWT_SECRET_KEY, Constants.JWT_SECRET_KEY_DEFAULT_VALUE);
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claim("email", email)
                .signWith(key)
                .compact();
    }
}
