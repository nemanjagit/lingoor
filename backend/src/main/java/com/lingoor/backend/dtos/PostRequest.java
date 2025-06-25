package com.lingoor.backend.dtos;

import jakarta.validation.constraints.NotBlank;

public record PostRequest(@NotBlank String word, @NotBlank String definition) {
}
