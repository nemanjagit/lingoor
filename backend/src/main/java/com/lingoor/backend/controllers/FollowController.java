package com.lingoor.backend.controllers;

import com.lingoor.backend.dtos.FollowResponse;
import com.lingoor.backend.exceptions.UnauthorizedException;
import com.lingoor.backend.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FollowController {

    private final FollowService followService;

    @GetMapping("/{id}/follow")
    public ResponseEntity<Map<String, Boolean>> isFollowing(
            @PathVariable("id") Long targetUserId,
            Principal principal
    ) {
        if (principal == null) throw new UnauthorizedException();
        boolean following = followService.isFollowing(principal.getName(), targetUserId);
        return ResponseEntity.ok(Map.of("following", following));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<?> toggleFollow(
            @PathVariable("id") Long targetUserId,
            Principal principal
    ) {
        if (principal == null) throw new UnauthorizedException();
        FollowResponse response = followService.toggleFollow(principal.getName(), targetUserId);
        if (response == null) {
            // Unfollowed — return new state false
            return ResponseEntity.ok(Map.of("following", false));
        }
        // Followed — return full response
        return ResponseEntity.ok(Map.of(
                "following", true,
                "followerId", response.followerId(),
                "followedId", response.followedId()
        ));
    }
}
