package com.lingoor.backend.dtos;

public record LikeToggleResponse(
        Long userId,
        Long postId,
        boolean liked,
        long likeCount
) {}
