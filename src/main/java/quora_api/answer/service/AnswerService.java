package quora_api.answer.service;

import java.util.UUID;

import quora_api.answer.dto.AnswerRequestDto;
import quora_api.answer.dto.AnswerResponseDto;
import quora_api.answer.dto.AnswerUpdateDto;

public interface AnswerService {
    AnswerResponseDto createAnswer(UUID questionId,AnswerRequestDto requestDto);

    AnswerResponseDto updateAnswer(AnswerUpdateDto updateDto);
}
