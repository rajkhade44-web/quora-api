package quora_api.answer.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import quora_api.answer.dto.AnswerRequestDto;
import quora_api.answer.dto.AnswerResponseDto;
import quora_api.answer.dto.AnswerUpdateDto;
import quora_api.answer.entity.Answer;
import quora_api.answer.mapper.AnswerMapper;
import quora_api.answer.repository.AnswerRepository;
import quora_api.answer.service.AnswerService;
import quora_api.common.exception.ResourceNotFoundException;
import quora_api.question.repository.QuestionRepository;
import quora_api.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerMapper answerMapper;


    @Override
    @Transactional
    public AnswerResponseDto createAnswer(UUID questionId, AnswerRequestDto requestDto) {
        if (!userRepository.existsById(requestDto.getUserId())) {
            throw new ResourceNotFoundException("User not found");
        }

        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found");
        }

        Answer answer = answerMapper.toEntity(requestDto);
        return answerMapper.toResponseDto(answerRepository.save(answer));
    }

    @Override
    @Transactional
    public AnswerResponseDto updateAnswer(AnswerUpdateDto updateDto) {
        Answer answer = answerRepository.findById(updateDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found"));

        if (answer.getUserId().equals(updateDto.getUserId())) {
            throw new IllegalArgumentException("You can only edit your own answers");
        }

        answer.setBody(updateDto.getText());
        return answerMapper.toResponseDto(answerRepository.save(answer));
    }

}
