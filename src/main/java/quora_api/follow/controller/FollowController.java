package quora_api.follow.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import quora_api.common.dto.ApiResponse;
import quora_api.follow.service.FollowService;
import quora_api.security.utils.SecurityUtils;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/follow/{targetUserId}")
    public ApiResponse<String> follow(@PathVariable UUID targetUserId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        followService.follow(userId, targetUserId);
        return ApiResponse.success(null, "Followed successfully");
    }
}
