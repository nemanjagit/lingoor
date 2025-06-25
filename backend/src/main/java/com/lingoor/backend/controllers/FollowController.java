package com.lingoor.backend.controllers;

import com.lingoor.backend.dtos.FollowResponse;
import com.lingoor.backend.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controller to manage follow/unfollow actions.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FollowController {

    private final FollowService followService;

    /**
     * Follow a user by ID.
     * @param id the ID of the user to follow
     * @param principal authenticated user principal
     */
    @PostMapping("/{id}/follow")
    public ResponseEntity<FollowResponse> followUser(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        FollowResponse response = followService.followUser(principal.getName(), id);
        return ResponseEntity.ok(response);
    }

    /**
     * Unfollow a user by ID.
     * @param id the ID of the user to unfollow
     * @param principal authenticated user principal
     */
    @DeleteMapping("/{id}/follow")
    public ResponseEntity<FollowResponse> unfollowUser(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        FollowResponse response = followService.unfollowUser(principal.getName(), id);
        return ResponseEntity.ok(response);
    }
}
