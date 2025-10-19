package com.lingoor.backend.services;

import com.lingoor.backend.dtos.PostRequest;
import com.lingoor.backend.dtos.PostResponse;
import com.lingoor.backend.dtos.FeedPostResponse;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest request, String currentUserEmail);

    PostResponse updatePost(PostRequest request, Long id, String currentUserEmail);

    void deletePost(Long id, String currentUserEmail);

    List<FeedPostResponse> getCommunityFeed(String currentUserEmail, int page, int size);

    List<FeedPostResponse> getPersonalizedFeed(String currentUserEmail, int page, int size);

    void setWordOfTheDay(Long postId);

    FeedPostResponse getWordOfTheDay(String currentUserEmail);
}
