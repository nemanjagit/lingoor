package com.lingoor.backend.controllers;

import com.lingoor.backend.dtos.FeedPostResponse;
import com.lingoor.backend.dtos.PostRequest;
import com.lingoor.backend.dtos.PostResponse;
import com.lingoor.backend.exceptions.UnauthorizedException;
import com.lingoor.backend.services.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @GetMapping
    public ResponseEntity<List<FeedPostResponse>> getCommunityFeed(
            Principal principal,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        String email = (principal != null) ? principal.getName() : null;
        return ResponseEntity.ok(
                postService.getCommunityFeed(email, query, author, sort, from, to, page, size)
        );
    }

    @GetMapping("/personalized")
    public ResponseEntity<List<FeedPostResponse>> getPersonalizedFeed(
            Principal principal,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        if (principal == null) throw new UnauthorizedException();
        return ResponseEntity.ok(
                postService.getPersonalizedFeed(principal.getName(), query, author, sort, from, to, page, size)
        );
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody @Valid PostRequest request,
            Principal principal
    ) {
        PostResponse created = postService.createPost(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostRequest request,
            Principal principal
    ) {
        PostResponse updated = postService.updatePost(request, id, principal.getName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            Principal principal
    ) {
        postService.deletePost(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/word-of-the-day")
    public ResponseEntity<FeedPostResponse> getWordOfTheDay(Principal principal) {
        String email = (principal != null) ? principal.getName() : null;
        FeedPostResponse response = postService.getWordOfTheDay(email);
        return ResponseEntity.ok(response);
    }
}
