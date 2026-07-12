package quora_api.comment.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import quora_api.answer.repository.AnswerRepository;
import quora_api.comment.dto.CommentRequestDto;
import quora_api.comment.dto.CommentResponseDto;
import quora_api.comment.entity.Comment;
import quora_api.comment.mapper.CommentMapper;
import quora_api.comment.repository.CommentRepository;
import quora_api.comment.service.CommentService;
import quora_api.common.exception.ResourceNotFoundException;
import quora_api.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentSeviceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDto commentOnAnswer(UUID answerId, CommentRequestDto request) {
        if (userRepository.existsById(request.getUserId()))
            throw new ResourceNotFoundException("User not found");
        
        if (answerRepository.existsById(answerId))
            throw new ResourceNotFoundException("Answer not found");

        Comment comment = commentMapper.toEntityForAnswer(request, answerId);
        return commentMapper.toResponseDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponseDto commentOnComment(UUID commentId, CommentRequestDto request) {
        if (!userRepository.existsById(request.getUserId()))
            throw new ResourceNotFoundException("User not found");
        if (!commentRepository.existsById(commentId))
            throw new ResourceNotFoundException("Parent comment not found");

        Comment comment = commentMapper.toEntityForComment(request, commentId);
        return commentMapper.toResponseDto(commentRepository.save(comment));
    }

}
