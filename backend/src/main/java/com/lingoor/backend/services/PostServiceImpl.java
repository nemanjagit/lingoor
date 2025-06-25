package com.lingoor.backend.services;

import com.lingoor.backend.dtos.PostRequest;
import com.lingoor.backend.dtos.PostResponse;
import com.lingoor.backend.mappers.PostMapper;
import com.lingoor.backend.models.Post;
import com.lingoor.backend.models.User;
import com.lingoor.backend.repositories.PostRepository;
import com.lingoor.backend.repositories.UserRepository;
import com.lingoor.backend.exceptions.ResourceNotFoundException;
import com.lingoor.backend.exceptions.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostResponse createPost(PostRequest dto, String authorEmail) {
        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(ResourceNotFoundException::new);
        Post post = PostMapper.toEntity(dto, author);
        Post saved = postRepository.save(post);
        return PostMapper.toDto(saved);
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostResponse updatePost(PostRequest request, Long id, String authorEmail) {
        Post post = postRepository.findByIdAndAuthor_Email(id, authorEmail)
                .orElseThrow(UnauthorizedException::new);
        post.setWord(request.word());
        post.setDefinition(request.definition());
        Post updated = postRepository.save(post);
        return PostMapper.toDto(updated);
    }

    @Override
    public void deletePost(Long id, String authorEmail) {
        boolean exists = postRepository.existsByIdAndAuthor_Email(id, authorEmail);
        if (!exists) {
            throw new UnauthorizedException();
        }
        postRepository.deleteById(id);
    }
}
