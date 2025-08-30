package com.lingoor.backend.controllers;

import com.lingoor.backend.dtos.FollowResponse;
import com.lingoor.backend.exceptions.UnauthorizedException;
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

    @PostMapping("/{id}/follow")
    public ResponseEntity<FollowResponse> followUser(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        if (principal == null) throw new UnauthorizedException();
        FollowResponse response = followService.followUser(principal.getName(), id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/follow")
    public ResponseEntity<FollowResponse> unfollowUser(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        if (principal == null) throw new UnauthorizedException();
        FollowResponse response = followService.unfollowUser(principal.getName(), id);
        return ResponseEntity.ok(response);
    }
}
