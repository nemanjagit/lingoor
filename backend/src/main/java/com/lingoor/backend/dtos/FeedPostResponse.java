package com.lingoor.backend.dtos;

import java.time.LocalDateTime;

public record FeedPostResponse(
        Long id,
        String word,
        String definition,
        LocalDateTime createdAt,
        String authorUsername,
        Long authorId,
        Long likeCount,
        boolean likedByMe
) {}