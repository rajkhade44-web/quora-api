package quora_api.like.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import quora_api.common.dto.ApiResponse;
import quora_api.like.service.LikeService;
import quora_api.like.service.impl.LikeServiceImpl;
import quora_api.security.utils.SecurityUtils;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{type}/{id}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> like(@PathVariable String type, @PathVariable UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        likeService.like(id, type,currentUserId);
        return ApiResponse.success(null, "Liked Successfully");
    }
}
