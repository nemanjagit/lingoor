package com.lingoor.backend.services;

import com.lingoor.backend.dtos.LikeToggleResponse;

public interface LikeService {

    LikeToggleResponse toggleLike(Long postId, String currentUserEmail);

    long countLikes(Long postId);

    boolean isLikedByUser(Long postId, String currentUserEmail);

}