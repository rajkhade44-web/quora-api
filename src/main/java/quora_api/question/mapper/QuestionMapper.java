package quora_api.question.mapper;

import org.springframework.stereotype.Component;

import quora_api.question.dto.QuestionRequestDto;
import quora_api.question.dto.QuestionResponseDto;
import quora_api.question.entity.Question;
import quora_api.question.entity.Topic;

@Component
public class QuestionMapper {
    public Question toEntity(QuestionRequestDto dto) {
        // removed user id from following dto
        return Question.builder()
                .body(dto.getBody())
                .title(dto.getTitle())
                .build();
    }

    public QuestionResponseDto toResponseDto(Question entity) {
        return QuestionResponseDto.builder()
                .body(entity.getBody())
                .title(entity.getTitle())
                .id(entity.getId())
                .userId(entity.getUserId())
                .createdAt(entity.getCreatedAt())
                .topicNames(entity.getTopics().stream().map(Topic::getName).toList())
                .build();
    }
}
