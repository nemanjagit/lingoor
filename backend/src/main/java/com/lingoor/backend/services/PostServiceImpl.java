package com.lingoor.backend.services;

import com.lingoor.backend.dtos.FeedPostResponse;
import com.lingoor.backend.dtos.PostRequest;
import com.lingoor.backend.dtos.PostResponse;
import com.lingoor.backend.mappers.PostMapper;
import com.lingoor.backend.models.Follow;
import com.lingoor.backend.models.Post;
import com.lingoor.backend.models.User;
import com.lingoor.backend.repositories.FollowRepository;
import com.lingoor.backend.repositories.LikeRepository;
import com.lingoor.backend.repositories.PostRepository;
import com.lingoor.backend.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    public PostServiceImpl(
            PostRepository postRepository,
            UserRepository userRepository,
            LikeRepository likeRepository,
            FollowRepository followRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.followRepository = followRepository;
    }

    @Override
    @Transactional
    public PostResponse createPost(PostRequest request, String currentUserEmail) {
        User author = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserEmail));
        Post post = PostMapper.toEntity(request, author);
        Post saved = postRepository.save(post);
        return PostMapper.toPostResponse(saved);
    }

    @Override
    @Transactional
    public PostResponse updatePost(PostRequest request, Long id, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserEmail));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new IllegalStateException("Forbidden");
        }
        post.setWord(request.word());
        post.setDefinition(request.definition());
        Post saved = postRepository.save(post);
        return PostMapper.toPostResponse(saved);
    }

    @Override
    @Transactional
    public void deletePost(Long id, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserEmail));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new IllegalStateException("Forbidden");
        }
        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedPostResponse> getCommunityFeed(String currentUserEmail, int page, int size) {
        Long currentUserId;
        if (currentUserEmail != null) {
            currentUserId = userRepository.findByEmail(currentUserEmail)
                    .map(User::getId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserEmail));
        } else {
            currentUserId = null;
        }
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findAll(pageable)
                .map(p -> {
                    long likeCount = likeRepository.countByPostId(p.getId());
                    boolean likedByMe = currentUserId != null && likeRepository.existsByUserIdAndPostId(currentUserId, p.getId());
                    return PostMapper.toFeedPostResponse(p, likeCount, likedByMe);
                })
                .getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedPostResponse> getPersonalizedFeed(String currentUserEmail, int page, int size) {
        if (currentUserEmail == null) return Collections.emptyList();
        Long currentUserId = userRepository.findByEmail(currentUserEmail)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserEmail));
        List<Follow> follows = followRepository.findAllByFollower_Id(currentUserId);
        if (follows.isEmpty()) return Collections.emptyList();
        List<Long> authorIds = follows.stream().map(f -> f.getFollowed().getId()).toList();
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findByAuthor_IdIn(authorIds, pageable)
                .map(p -> {
                    long likeCount = likeRepository.countByPostId(p.getId());
                    boolean likedByMe = likeRepository.existsByUserIdAndPostId(currentUserId, p.getId());
                    return PostMapper.toFeedPostResponse(p, likeCount, likedByMe);
                })
                .getContent();
    }
}
