package com.lingoor.backend.repositories;

import com.lingoor.backend.models.Follow;
import com.lingoor.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollower_IdAndFollowed_Id(Long followerId, Long followedId);

    void deleteByFollower_IdAndFollowed_Id(Long followerId, Long followedId);

    List<User> findFollowedByFollower_Id(Long followerId);

    List<Follow> findAllByFollower_Id(Long followerId);
}
