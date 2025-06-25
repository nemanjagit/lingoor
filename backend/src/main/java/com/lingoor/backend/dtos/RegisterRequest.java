package com.lingoor.backend.dtos;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank String email, @NotBlank String username, @NotBlank String password) {
}
