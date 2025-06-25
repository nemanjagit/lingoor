package com.lingoor.backend.mappers;

import com.lingoor.backend.dtos.FollowResponse;
import com.lingoor.backend.models.Follow;
import com.lingoor.backend.models.User;

/**
 * Utility for converting Follow entities to DTOs.
 */
public class FollowMapper {

    /**
     * Map a Follow entity to a FollowResponse DTO.
     *
     * @param follow the Follow entity
     * @return FollowResponse containing follower and followed IDs
     */
    public static FollowResponse toDto(Follow follow) {
        return new FollowResponse(
                follow.getFollower().getId(),
                follow.getFollowed().getId()
        );
    }

    public static Follow toEntity(User follower, User followed) {
        return Follow.builder()
                .follower(follower)
                .followed(followed)
                .build();
    }
}