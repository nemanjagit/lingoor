package com.lingoor.backend.repositories;

import com.lingoor.backend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    Optional<Post> findByIdAndAuthor_Email(Long id, String authorEmail);

    boolean existsByIdAndAuthor_Email(Long id, String authorEmail);
}
