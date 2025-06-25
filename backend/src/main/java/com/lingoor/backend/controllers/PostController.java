package com.lingoor.backend.controllers;

import com.lingoor.backend.dtos.PostRequest;
import com.lingoor.backend.dtos.PostResponse;
import com.lingoor.backend.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    /**
     * Retrieve all posts, newest first
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Create a new post
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody @Valid PostRequest request,
            Principal principal
    ) {
        PostResponse created = postService.createPost(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing post (must be the author)
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostRequest request,
            Principal principal
    ) {
        PostResponse updated = postService.updatePost(request, id, principal.getName());
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a post by id (must be the author)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            Principal principal
    ) {
        postService.deletePost(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}