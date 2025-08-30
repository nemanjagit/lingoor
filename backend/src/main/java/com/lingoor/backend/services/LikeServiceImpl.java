package com.lingoor.backend.services;

import com.lingoor.backend.dtos.LikeToggleResponse;
import com.lingoor.backend.models.Like;
import com.lingoor.backend.models.Post;
import com.lingoor.backend.models.User;
import com.lingoor.backend.repositories.LikeRepository;
import com.lingoor.backend.repositories.PostRepository;
import com.lingoor.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public LikeToggleResponse toggleLike(Long postId, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserEmail));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));

        Long uid = user.getId();

        if (likeRepository.existsByUserIdAndPostId(uid, postId)) {
            likeRepository.deleteByUserIdAndPostId(uid, postId);
            long count = likeRepository.countByPostId(postId);
            return new LikeToggleResponse(uid, postId, false, count);
        }

        Like like = Like.builder().user(user).post(post).build();
        likeRepository.save(like);
        long count = likeRepository.countByPostId(postId);
        return new LikeToggleResponse(uid, postId, true, count);
    }

    @Override
    public long countLikes(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public boolean isLikedByUser(Long postId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userEmail));
        return likeRepository.existsByUserIdAndPostId(user.getId(), postId);
    }
}
