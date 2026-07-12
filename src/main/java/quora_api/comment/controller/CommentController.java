package quora_api.comment.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import quora_api.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import quora_api.comment.dto.CommentRequestDto;
import quora_api.comment.dto.CommentResponseDto;
import quora_api.comment.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/answers/{answerId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentResponseDto> commentOnAnswer(@PathVariable UUID answerId,
            @Valid @RequestBody CommentRequestDto requestDto) {
        return ApiResponse.success(
                commentService.commentOnAnswer(answerId, requestDto),
                "Comment posted on answer");
    }
    
    @PostMapping("/comments/{commentId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentResponseDto> commentOnComment(@PathVariable UUID commentId,
            @Valid @RequestBody CommentRequestDto request) {
        return ApiResponse.success(commentService.commentOnComment(commentId, request),
                "Reply posted on comment");
    }
    
}
