package quora_api.answer.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import quora_api.answer.dto.AnswerRequestDto;
import quora_api.answer.dto.AnswerResponseDto;
import quora_api.answer.entity.Answer;
import quora_api.security.utils.SecurityUtils;

@Component
public class AnswerMapper {

    public Answer toEntity(AnswerRequestDto requestDto) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return Answer.builder()
                .userId(userId)
                .body(requestDto.getText())
                .build();
    }

    public AnswerResponseDto toResponseDto(Answer entity) {
        return AnswerResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .questionId(entity.getQuestionId())
                .text(entity.getBody())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
