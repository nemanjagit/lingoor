package com.lingoor.backend.services;

import com.lingoor.backend.constants.Constants;
import com.lingoor.backend.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final Environment env;

    public String generateToken(User user) {
        String jwtSecret = env.getProperty(Constants.JWT_SECRET_KEY, Constants.JWT_SECRET_KEY_DEFAULT_VALUE);
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24h token
                .signWith(key)
                .compact();
    }
}
