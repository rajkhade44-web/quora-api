package quora_api.question.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import quora_api.common.exception.ResourceNotFoundException;
import quora_api.question.dto.QuestionRequestDto;
import quora_api.question.dto.QuestionResponseDto;
import quora_api.question.entity.Question;
import quora_api.question.entity.Topic;
import quora_api.question.mapper.QuestionMapper;
import quora_api.question.repository.QuestionRepository;
import quora_api.question.repository.TopicRepository;
import quora_api.question.service.QuestionService;
import quora_api.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final QuestionMapper mapper;


    @Override
    @Transactional
    public QuestionResponseDto createQuestion(QuestionRequestDto requestDto) {
        if(!userRepository.existsById(requestDto.getUserId()))
            throw new ResourceNotFoundException("User not found");

        Question question = mapper.toEntity(requestDto);

        if (requestDto.getTopicTags() != null && !requestDto.getTopicTags().isEmpty()) {
            Set<Topic> topics = new HashSet<>();
            for (String tag : requestDto.getTopicTags()) {
                Topic topic = topicRepository.findByName(tag)
                        .orElseGet(() -> topicRepository.save(new Topic(null, tag, null)));
            }
            question.setTopics(topics);
        }

        Question savedQuestion = questionRepository.save(question);
        return mapper.toResponseDto(savedQuestion);
    }

    @Override
    public Page<QuestionResponseDto> searchQuestions(String text, String tag, Pageable pageable) {
        Page<Question> questionPage = questionRepository.searchWithTopics(text, tag, pageable);
        return questionPage.map(mapper::toResponseDto);
    }

}
