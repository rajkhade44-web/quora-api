package quora_api.like.service;

import java.util.UUID;

import quora_api.common.enums.LikeTargetType;
import quora_api.like.dto.LikeRequestDto;

public interface LikeService {
    void like(UUID targetId, String targetType, LikeRequestDto requestDto);
} 
