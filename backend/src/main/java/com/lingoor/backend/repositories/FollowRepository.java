package com.lingoor.backend.repositories;

import com.lingoor.backend.models.Follow;
import com.lingoor.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * Does a follow relationship exist between the given follower email and followed user ID?
     */
    boolean existsByFollower_EmailAndFollowed_Id(String followerEmail, Long followedId);

    /**
     * Remove the follow relationship for the given follower email and followed user ID.
     */
    void deleteByFollower_EmailAndFollowed_Id(String followerEmail, Long followedId);

    /**
     * Fetch all User entities that are followed by the given follower email.
     */
    @Query("select f.followed from Follow f where f.follower.email = :email")
    List<User> findFollowedUsersByFollowerEmail(@Param("email") String followerEmail);

    /**
     * Fetch all Follow entities for the given follower email (can be used if you need metadata).
     */
    List<Follow> findAllByFollower_Email(String followerEmail);
}
