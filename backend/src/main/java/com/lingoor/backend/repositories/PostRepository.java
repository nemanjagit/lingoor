package com.lingoor.backend.repositories;

import com.lingoor.backend.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAll(Pageable pageable); // (optional; JpaRepository already has this)

    Page<Post> findByAuthor_IdIn(List<Long> authorIds, Pageable pageable);

    boolean existsByIdAndAuthor_Email(Long id, String authorEmail);

    @Query("SELECT p FROM Post p LEFT JOIN p.likes l GROUP BY p.id ORDER BY COUNT(l) DESC")
    List<Post> findTop10MostLiked();
}
