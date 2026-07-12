package quora_api.answer.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import quora_api.answer.dto.AnswerRequestDto;
import quora_api.answer.dto.AnswerResponseDto;
import quora_api.answer.dto.AnswerUpdateDto;
import quora_api.answer.entity.Answer;
import quora_api.answer.service.AnswerService;
import quora_api.common.dto.ApiResponse;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/questions/{questionId}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AnswerResponseDto> postAnswer(@Valid @RequestBody AnswerRequestDto requestDto,
            @PathVariable UUID questionId) {
        return ApiResponse.success(answerService.createAnswer(questionId, requestDto), "Answer posted successfully");
    }
    
    @PostMapping("/answers")
    public ApiResponse<AnswerResponseDto> updateAnswer(@RequestBody AnswerUpdateDto updateDto) {
        return ApiResponse.success(answerService.updateAnswer(updateDto), "Answer updated successfully");
    }

}
