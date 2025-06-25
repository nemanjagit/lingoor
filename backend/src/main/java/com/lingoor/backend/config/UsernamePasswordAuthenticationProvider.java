package com.lingoor.backend.config;

import com.lingoor.backend.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Extract the input identifier (could be username or email)
        String identifier = authentication.getName();

        // Extract the raw password entered by the user
        String password = authentication.getCredentials().toString();

        // Load the user by the identifier (username or email)
        // This assumes your UserDetailsService implementation supports both
        UserDetails userDetails = userDetailsService.loadUserByUsername(identifier);

        // Compare the raw password with the encoded one from the database
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            // If the passwords match, create and return an authenticated token
            return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), password, userDetails.getAuthorities());
        } else {
            // If the password is invalid, throw an exception
            throw new BadCredentialsException(Constants.INVALID_CREDENTIALS);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // This AuthenticationProvider supports UsernamePasswordAuthenticationToken
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
