package quora_api.question.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import quora_api.question.dto.QuestionRequestDto;
import quora_api.question.dto.QuestionResponseDto;
import quora_api.question.dto.QuestionUpdateDto;

public interface QuestionService {
    QuestionResponseDto createQuestion(UUID userId, QuestionRequestDto requestDto);

    Page<QuestionResponseDto> searchQuestions(String text, String tag, Pageable pageable);

    QuestionResponseDto getQuestionById(UUID id);

    QuestionResponseDto updateQuestion(UUID id, QuestionUpdateDto dto);

    void deleteQuestion(UUID id);
}
