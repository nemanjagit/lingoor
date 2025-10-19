package com.lingoor.backend.repositories;

import com.lingoor.backend.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAll(Pageable pageable); // (optional; JpaRepository already has this)

    Page<Post> findByAuthor_IdIn(List<Long> authorIds, Pageable pageable);

    boolean existsByIdAndAuthor_Email(Long id, String authorEmail);

    @Query("SELECT p FROM Post p LEFT JOIN p.likes l GROUP BY p.id ORDER BY COUNT(l) DESC")
    List<Post> findTop10MostLiked();

    @Query("""
    SELECT p FROM Post p
    WHERE (:query IS NULL OR LOWER(p.word) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(p.definition) LIKE LOWER(CONCAT('%', :query, '%')))
      AND (:author IS NULL OR LOWER(p.author.username) = LOWER(:author))
      AND (:from IS NULL OR p.createdAt >= :from)
      AND (:to IS NULL OR p.createdAt <= :to)
    """)
    Page<Post> searchPosts(@Param("query") String query,
                           @Param("author") String author,
                           @Param("from") LocalDate from,
                           @Param("to") LocalDate to,
                           Pageable pageable);

    @Query("""
    SELECT p FROM Post p
    WHERE (:query IS NULL OR LOWER(p.word) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(p.definition) LIKE LOWER(CONCAT('%', :query, '%')))
      AND (:author IS NULL OR LOWER(p.author.username) = LOWER(:author))
      AND (:fromDate IS NULL OR p.createdAt >= :fromDate)
      AND (:toDate IS NULL OR p.createdAt <= :toDate)
      AND (:authorIds IS NULL OR p.author.id IN :authorIds)
    """)
    Page<Post> searchPostsWithAuthors(
            @Param("query") String query,
            @Param("author") String author,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("authorIds") List<Long> authorIds,
            Pageable pageable
    );

}
