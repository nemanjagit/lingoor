package com.lingoor.backend.services;

import com.lingoor.backend.dtos.AuthResponse;
import com.lingoor.backend.dtos.AuthRequest;

public interface UserService {
    AuthResponse register(AuthRequest registerRequest);
    AuthResponse login(AuthRequest loginRequest);

}
