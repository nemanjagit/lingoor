package com.lingoor.backend.services;

import com.lingoor.backend.dtos.AuthResponse;
import com.lingoor.backend.dtos.LoginRequest;
import com.lingoor.backend.dtos.RegisterRequest;
import com.lingoor.backend.exceptions.EmailTakenException;
import com.lingoor.backend.exceptions.InvalidCredentialsException;
import com.lingoor.backend.exceptions.UsernameTakenException;
import com.lingoor.backend.models.Role;
import com.lingoor.backend.models.User;
import com.lingoor.backend.repositories.UserRepository;
import com.lingoor.backend.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailTakenException();
        }
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UsernameTakenException();
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(userRole)
                .build();

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }


    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }
}
