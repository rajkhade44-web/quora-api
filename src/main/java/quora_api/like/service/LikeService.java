package quora_api.like.service;

import java.util.UUID;

import quora_api.common.enums.LikeTargetType;

public interface LikeService {
    void like(UUID targetId, String targetType, UUID currentUserId);
} 
