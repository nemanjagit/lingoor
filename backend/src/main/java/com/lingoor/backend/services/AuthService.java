package com.lingoor.backend.services;

import com.lingoor.backend.dtos.AuthResponse;
import com.lingoor.backend.dtos.LoginRequest;
import com.lingoor.backend.dtos.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);

}
