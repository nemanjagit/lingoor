package com.lingoor.backend.dtos;

import java.time.LocalDateTime;

public record PostResponse (Long id, String word, String definition,
                            LocalDateTime createdAt, String authorUsername){
}
