package com.lingoor.backend.services;

import com.lingoor.backend.dtos.FollowResponse;

import java.util.List;

public interface FollowService {

    boolean isFollowing(String followerEmail, Long followedId);

    FollowResponse toggleFollow(String followerEmail, Long followedId);

    List<String> getFollowedUserEmails(String followerEmail);
}
