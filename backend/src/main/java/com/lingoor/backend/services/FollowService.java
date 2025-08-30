package com.lingoor.backend.services;

import com.lingoor.backend.dtos.FollowResponse;

import java.util.List;

public interface FollowService {

    FollowResponse followUser(String followerEmail, Long followedId);

    FollowResponse unfollowUser(String followerEmail, Long followedId);

    List<String> getFollowedUserEmails(String followerEmail);
}
