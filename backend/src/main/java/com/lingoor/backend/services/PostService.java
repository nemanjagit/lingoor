package com.lingoor.backend.services;

import com.lingoor.backend.dtos.PostRequest;
import com.lingoor.backend.dtos.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest request, String email);
    PostResponse updatePost(PostRequest request, Long id, String email);
    void deletePost(Long id, String email);
    List<PostResponse> getAllPosts();
}
