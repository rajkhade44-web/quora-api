package quora_api.comment.service;

import java.util.UUID;

import quora_api.answer.dto.AnswerUpdateDto;
import quora_api.comment.dto.CommentRequestDto;
import quora_api.comment.dto.CommentResponseDto;

public interface CommentService {
    CommentResponseDto commentOnAnswer(UUID answerId, CommentRequestDto request);
    CommentResponseDto commentOnComment(UUID commentId, CommentRequestDto request);    
} 
