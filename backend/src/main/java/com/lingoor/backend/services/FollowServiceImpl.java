package com.lingoor.backend.services;

import com.lingoor.backend.dtos.FollowResponse;
import com.lingoor.backend.exceptions.AlreadyFollowingException;
import com.lingoor.backend.exceptions.ResourceNotFoundException;
import com.lingoor.backend.mappers.FollowMapper;
import com.lingoor.backend.models.User;
import com.lingoor.backend.repositories.FollowRepository;
import com.lingoor.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    public FollowResponse followUser(String followerEmail, Long followedId) {
        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(ResourceNotFoundException::new);
        User followed = userRepository.findById(followedId)
                .orElseThrow(ResourceNotFoundException::new);

        if (followRepository.existsByFollower_IdAndFollowed_Id(follower.getId(), followedId)) {
            throw new AlreadyFollowingException(followedId);
        }

        var follow = FollowMapper.toEntity(follower, followed);
        var saved = followRepository.save(follow);
        return FollowMapper.toDto(saved);
    }

    @Override
    public FollowResponse unfollowUser(String followerEmail, Long followedId) {
        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(ResourceNotFoundException::new);
        User followed = userRepository.findById(followedId)
                .orElseThrow(ResourceNotFoundException::new);

        if (followRepository.existsByFollower_IdAndFollowed_Id(follower.getId(), followedId)) {
            followRepository.deleteByFollower_IdAndFollowed_Id(follower.getId(), followedId);
        }

        return new FollowResponse(follower.getId(), followed.getId());
    }

    @Override
    public List<String> getFollowedUserEmails(String followerEmail) {
        Long followerId = userRepository.findByEmail(followerEmail)
                .map(User::getId)
                .orElseThrow(ResourceNotFoundException::new);

        return followRepository.findFollowedByFollower_Id(followerId)
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }
}
