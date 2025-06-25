package com.lingoor.backend.services;

import com.lingoor.backend.dtos.FollowResponse;

import java.util.List;

public interface FollowService {

    /**
     * Establish a follow relationship: follower follows the user with ID followedId.
     * @param followerEmail the email of the user who wants to follow
     * @param followedId the ID of the user to be followed
     */
    FollowResponse followUser(String followerEmail, Long followedId);

    /**
     * Remove a follow relationship: follower unfollows the user with ID followedId.
     * @param followerEmail the email of the user who wants to unfollow
     * @param followedId the ID of the user to be unfollowed
     */
    FollowResponse unfollowUser(String followerEmail, Long followedId);

    /**
     * Retrieve the list of emails that the given user is following.
     * @param followerEmail the email of the user whose followings to retrieve
     * @return list of emails of the followed users
     */
    List<String> getFollowedUserEmails(String followerEmail);
}
