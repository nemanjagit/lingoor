package com.lingoor.backend.mappers;

import com.lingoor.backend.dtos.PostRequest;
import com.lingoor.backend.dtos.PostResponse;
import com.lingoor.backend.models.Post;
import com.lingoor.backend.models.User;

public class PostMapper {
    public static PostResponse toDto(Post post) {
        return new PostResponse(
                post.getId(),
                post.getWord(),
                post.getDefinition(),
                post.getCreatedAt(),
                post.getAuthor().getUsername(),
                post.getLikeCount()
        );
    }

    public static Post toEntity(PostRequest req, User author) {
        return Post.builder()
                .word(req.word())
                .definition(req.definition())
                .author(author)
                .build();
    }
}
