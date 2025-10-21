package com.lingoor.backend.mappers;

import com.lingoor.backend.dtos.PostRequest;
import com.lingoor.backend.dtos.PostResponse;
import com.lingoor.backend.dtos.FeedPostResponse;
import com.lingoor.backend.models.Post;
import com.lingoor.backend.models.User;

public class PostMapper {

    public static Post toEntity(PostRequest req, User author) {
        return Post.builder()
                .word(req.word())
                .definition(req.definition())
                .author(author)
                .build();
    }

    public static PostResponse toPostResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getWord(),
                post.getDefinition(),
                post.getCreatedAt(),
                post.getAuthor().getUsername()
        );
    }

    public static FeedPostResponse toFeedPostResponse(Post post, long likeCount, boolean likedByMe, boolean followingAuthor) {
        return new FeedPostResponse(
                post.getId(),
                post.getWord(),
                post.getDefinition(),
                post.getCreatedAt(),
                post.getAuthor().getUsername(),
                post.getAuthor().getId(),
                likeCount,
                likedByMe,
                followingAuthor
        );
    }
}
