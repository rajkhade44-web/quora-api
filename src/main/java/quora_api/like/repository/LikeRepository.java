package quora_api.like.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import quora_api.common.enums.LikeTargetType;
import quora_api.like.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like,UUID>{
    Optional<Like> findByUserIdAndTargetTypeAndTargetId(UUID userId, LikeTargetType targetType, UUID targetId);

    boolean existsByUserIdAndTargetTypeAndTargetId(UUID userId, LikeTargetType targetType, UUID targetId);
}
