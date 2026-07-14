package quora_api.follow.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import quora_api.common.exception.ResourceNotFoundException;
import quora_api.follow.entity.Follow;
import quora_api.follow.repository.FollowRepository;
import quora_api.follow.service.FollowService;
import quora_api.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{
    private final UserRepository userRepository;
    private final FollowRepository followRepository;


    @Override
    public void follow(UUID followerId, UUID followeeId) {

        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }

        if (!userRepository.existsById(followerId)) {
            throw new ResourceNotFoundException("Follower user does not exists");
        }

        if (followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            return;
        }

        Follow follow = Follow.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();
        
        followRepository.save(follow);

    }


}
