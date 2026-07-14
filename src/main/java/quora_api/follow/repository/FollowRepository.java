package quora_api.follow.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import quora_api.follow.entity.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow,UUID>{
    boolean existsByFollowerIdAndFolloweeId(UUID followerId, UUID followeeId);
}
