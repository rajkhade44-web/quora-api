package quora_api.question.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import quora_api.common.dto.ApiResponse;
import quora_api.question.dto.QuestionRequestDto;
import quora_api.question.dto.QuestionResponseDto;
import quora_api.question.repository.QuestionRepository;
import quora_api.question.service.QuestionService;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ApiResponse<QuestionResponseDto> postQuestion(@Valid @RequestBody QuestionRequestDto requestDto) {
        return ApiResponse.success(questionService.createQuestion(requestDto), "Question posted successfully");
    }

    @GetMapping("/search")
    public ApiResponse<Page<QuestionResponseDto>> search(
        @RequestParam(required = false) String text,
        @RequestParam(required = false) String tag,
        @PageableDefault(size=20,sort="createdAt",direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<QuestionResponseDto> page = questionService.searchQuestions(text, tag, pageable);
        return ApiResponse.success(page, "Search results");
    }
}
