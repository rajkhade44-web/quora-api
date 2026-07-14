package quora_api.follow.service;

import java.util.UUID;

public interface FollowService {
    void follow(UUID followerId, UUID followeeId);
}
