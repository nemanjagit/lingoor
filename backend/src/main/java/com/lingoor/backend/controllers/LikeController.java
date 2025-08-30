package com.lingoor.backend.controllers;

import com.lingoor.backend.dtos.LikeToggleResponse;
import com.lingoor.backend.exceptions.UnauthorizedException;
import com.lingoor.backend.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{id}/likes/toggle")
    public ResponseEntity<LikeToggleResponse> toggleLike(
            @PathVariable("id") Long postId,
            Principal principal
    ) {
        if (principal == null) throw new UnauthorizedException();
        return ResponseEntity.ok(likeService.toggleLike(postId, principal.getName()));
    }

    @GetMapping("/{id}/likes/count")
    public ResponseEntity<Long> countLikes(@PathVariable("id") Long postId) {
        return ResponseEntity.ok(likeService.countLikes(postId));
    }

    @GetMapping("/{id}/likes/me")
    public ResponseEntity<Boolean> isLikedByMe(
            @PathVariable("id") Long postId,
            Principal principal
    ) {
        if (principal == null) throw new UnauthorizedException();
        return ResponseEntity.ok(likeService.isLikedByUser(postId, principal.getName()));
    }
}