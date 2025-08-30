package com.lingoor.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank @Size(max = 64)  String word,
        @NotBlank @Size(max = 512) String definition
) {}
