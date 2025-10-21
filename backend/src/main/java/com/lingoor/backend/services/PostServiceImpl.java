package com.lingoor.backend.services;

import com.lingoor.backend.dtos.FeedPostResponse;
import com.lingoor.backend.dtos.PostRequest;
import com.lingoor.backend.dtos.PostResponse;
import com.lingoor.backend.exceptions.ResourceNotFoundException;
import com.lingoor.backend.mappers.PostMapper;
import com.lingoor.backend.models.DailyHighlight;
import com.lingoor.backend.models.Post;
import com.lingoor.backend.models.User;
import com.lingoor.backend.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;
    private final DailyHighlightRepository dailyHighlightRepository;

    public PostServiceImpl(
            PostRepository postRepository,
            UserRepository userRepository,
            LikeRepository likeRepository,
            FollowRepository followRepository, DailyHighlightRepository dailyHighlightRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.followRepository = followRepository;
        this.dailyHighlightRepository = dailyHighlightRepository;
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

        boolean isAdmin = user.getRole().getName().equals("ROLE_ADMIN");
        boolean isAuthor = post.getAuthor().getId().equals(user.getId());
        if (!isAdmin && !isAuthor) {
            throw new IllegalStateException("Forbidden");
        }
        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedPostResponse> getCommunityFeed(
            String currentUserEmail,
            String query,
            String sort,
            int page,
            int size
    ) {
        Long currentUserId;

        if (currentUserEmail != null) {
            currentUserId = userRepository.findByEmail(currentUserEmail)
                    .map(User::getId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserEmail));
        } else {
            currentUserId = null;
        }

        // Determine sorting option
        Sort sortOption = switch (sort == null ? "date" : sort) {
            case "oldest" -> Sort.by(Sort.Direction.ASC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };

        PageRequest pageable = PageRequest.of(page, size, sortOption);

        // Trim and normalize query
        String trimmedQuery = (query == null || query.isBlank()) ? null : query.trim();

        Page<Post> posts = postRepository.searchPostsWithAuthors(trimmedQuery, pageable);

        Long finalCurrentUserId = currentUserId; // effectively final for lambda

        return posts.map(p -> {
            long likeCount = likeRepository.countByPostId(p.getId());
            boolean likedByMe = finalCurrentUserId != null &&
                    likeRepository.existsByUserIdAndPostId(finalCurrentUserId, p.getId());
            boolean followingAuthor = currentUserId != null &&
                    followRepository.existsByFollower_IdAndFollowed_Id(currentUserId, p.getAuthor().getId());

            return PostMapper.toFeedPostResponse(p, likeCount, likedByMe, followingAuthor);
        }).getContent();
    }



    @Override
    @Transactional(readOnly = true)
    public List<FeedPostResponse> getPersonalizedFeed(
            String currentUserEmail,
            String query,
            String sort,
            int page,
            int size
    ) {
        if (currentUserEmail == null)
            return Collections.emptyList();

        Long currentUserId = userRepository.findByEmail(currentUserEmail)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserEmail));

        List<Long> followedIds = followRepository.findAllByFollower_Id(currentUserId)
                .stream()
                .map(f -> f.getFollowed().getId())
                .toList();

        if (followedIds.isEmpty())
            return Collections.emptyList();

        Sort sortOption = switch (sort == null ? "date" : sort) {
            case "oldest" -> Sort.by(Sort.Direction.ASC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };

        PageRequest pageable = PageRequest.of(page, size, sortOption);
        String trimmedQuery = (query == null || query.isBlank()) ? null : query.trim();

        Page<Post> posts = postRepository.searchPostsByAuthorsAndQuery(followedIds, trimmedQuery, pageable);

        return posts.map(p -> {
            long likeCount = likeRepository.countByPostId(p.getId());
            boolean likedByMe = likeRepository.existsByUserIdAndPostId(currentUserId, p.getId());
            boolean followingAuthor = currentUserId != null &&
                    followRepository.existsByFollower_IdAndFollowed_Id(currentUserId, p.getAuthor().getId());
            return PostMapper.toFeedPostResponse(p, likeCount, likedByMe, followingAuthor);
        }).getContent();
    }


    @Transactional
    public void setWordOfTheDay(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(ResourceNotFoundException::new);

        LocalDate today = LocalDate.now();

        DailyHighlight dailyHighlight = dailyHighlightRepository.findByDate(today).orElse(null);

        if (dailyHighlight != null) {
            dailyHighlight.setPost(post);
            dailyHighlightRepository.save(dailyHighlight);
        } else {
            DailyHighlight highlight = DailyHighlight.builder()
                    .date(today)
                    .post(post)
                    .build();
            dailyHighlightRepository.save(highlight);
        }
    }

    @Transactional(readOnly = true)
    public FeedPostResponse getWordOfTheDay(String currentUserEmail) {
        LocalDate today = LocalDate.now();

        DailyHighlight highlight = dailyHighlightRepository.findByDate(today)
                .orElseThrow(ResourceNotFoundException::new);

        Post post = highlight.getPost();

        long likeCount = likeRepository.countByPostId(post.getId());
        boolean likedByMe = false;

        if (currentUserEmail != null) {
            Long currentUserId = userRepository.findByEmail(currentUserEmail)
                    .map(User::getId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserEmail));

            likedByMe = likeRepository.existsByUserIdAndPostId(currentUserId, post.getId());
        }
        boolean followingAuthor = false; // not needed for WOTD

        return PostMapper.toFeedPostResponse(post, likeCount, likedByMe, followingAuthor);
    }


}
