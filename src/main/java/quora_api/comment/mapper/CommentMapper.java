package quora_api.comment.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import quora_api.comment.dto.CommentRequestDto;
import quora_api.comment.dto.CommentResponseDto;
import quora_api.comment.entity.Comment;

@Component
public class CommentMapper {

    public Comment toEntityForAnswer(CommentRequestDto requestDto, UUID parentId) {
        return Comment.builder()
                .userId(requestDto.getUserId())
                .body(requestDto.getText())
                .parentId(parentId)
                .parentType("answer")
                .build();
    }

    public Comment toEntityForComment(CommentRequestDto requestDto, UUID parentId) {
        return Comment.builder()
                .userId(requestDto.getUserId())
                .body(requestDto.getText())
                .parentId(parentId)
                .parentType("comment")
                .build();
    }

    public CommentResponseDto toResponseDto(Comment entity) {
        return CommentResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .text(entity.getBody())
                .parentId(entity.getParentId())
                .parentType(entity.getParentType())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
