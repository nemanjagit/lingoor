package com.lingoor.backend.services;

import com.lingoor.backend.dtos.FollowResponse;
import com.lingoor.backend.exceptions.ResourceNotFoundException;
import com.lingoor.backend.models.Follow;
import com.lingoor.backend.models.User;
import com.lingoor.backend.repositories.FollowRepository;
import com.lingoor.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    public boolean isFollowing(String followerEmail, Long followedId) {
        return followRepository.existsByFollower_EmailAndFollowed_Id(followerEmail, followedId);
    }

    @Override
    @Transactional
    public FollowResponse toggleFollow(String followerEmail, Long followedId) {
        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(ResourceNotFoundException::new);
        User followed = userRepository.findById(followedId)
                .orElseThrow(ResourceNotFoundException::new);

        Optional<Follow> existing = followRepository.findByFollowerAndFollowed(follower, followed);

        if (existing.isPresent()) {
            followRepository.delete(existing.get());
            return null; // indicates unfollowed
        } else {
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowed(followed);
            followRepository.save(follow);
            return new FollowResponse(follower.getId(), followed.getId()); // now following
        }
    }

    @Override
    public List<String> getFollowedUserEmails(String followerEmail) {
        return followRepository.findAllByFollower_Email(followerEmail)
                .stream()
                .map(f -> f.getFollowed().getEmail())
                .toList();
    }
}
