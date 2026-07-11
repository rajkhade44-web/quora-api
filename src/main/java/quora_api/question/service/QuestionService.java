package quora_api.question.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import quora_api.question.dto.QuestionRequestDto;
import quora_api.question.dto.QuestionResponseDto;

public interface QuestionService {
    QuestionResponseDto createQuestion(QuestionRequestDto requestDto);

    Page<QuestionResponseDto> searchQuestions(String text, String tag, Pageable pageable);
}
