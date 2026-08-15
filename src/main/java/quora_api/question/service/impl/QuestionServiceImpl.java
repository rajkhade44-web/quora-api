package quora_api.question.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quora_api.common.exception.ResourceNotFoundException;
import quora_api.question.dto.QuestionRequestDto;
import quora_api.question.dto.QuestionResponseDto;
import quora_api.question.dto.QuestionUpdateDto;
import quora_api.question.entity.Question;
import quora_api.question.entity.Topic;
import quora_api.question.mapper.QuestionMapper;
import quora_api.question.repository.QuestionRepository;
import quora_api.question.repository.TopicRepository;
import quora_api.question.service.QuestionService;
import quora_api.security.utils.SecurityUtils;
import quora_api.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final QuestionMapper mapper;


    @Override
    @Transactional
    public QuestionResponseDto createQuestion(UUID userId,QuestionRequestDto requestDto) {
        if(!userRepository.existsById(userId))
            throw new ResourceNotFoundException("User not found");

        Question question = mapper.toEntity(requestDto);

        if (requestDto.getTopicTags() != null && !requestDto.getTopicTags().isEmpty()) {
            Set<Topic> topics = new HashSet<>();
            for (String tag : requestDto.getTopicTags()) {
                Topic topic = topicRepository.findByName(tag)
                        .orElseGet(() -> topicRepository.save(new Topic(null, tag, new HashSet<>())));
                // log.info("Topics related to question: "+question.getTopics());
                question.addTopic(topic);
                // log.info("(QuestionServiceImpl)Topics saved are : "+topic);
            }

            // question.setTopics(topics);
        }
        log.info("Topics count = {}", question.getTopics().size());

        Question savedQuestion = questionRepository.save(question);
        // savedQuestion.getTopics().forEach(topic ->log.info("Question={} -> Topic={}", savedQuestion.getTitle(), topic.getName()));
        return mapper.toResponseDto(savedQuestion);
    }

    @Override
    @Cacheable(value = "questionSearch", key = "{#text, #tag, #pageable.pageNumber, #pageable.pageSize}")
    @Transactional(readOnly = true)
    public Page<QuestionResponseDto> searchQuestions(String text, String tag, Pageable pageable) {
        Page<Question> questionPage = questionRepository.searchWithTopics(text, tag, pageable);
        return questionPage.map(mapper::toResponseDto);
    }

    @Override
    @Cacheable(value = "questions", key = "#id")
    @Transactional(readOnly = true)
    public QuestionResponseDto getQuestionById(UUID id) {
        Question question = questionRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        return mapper.toResponseDto(question);
    }

    @Override
    @CacheEvict(value = { "questions", "questionSearch" }, allEntries = true)
    @Transactional
    public QuestionResponseDto updateQuestion(UUID id, QuestionUpdateDto dto) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        Question dbQuestion = questionRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        
        if (!dbQuestion.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("You can only edit your own question");
        }

        if(dto.getTitle()!=null) dbQuestion.setTitle(dto.getTitle());
        if (dto.getBody() != null)
            dbQuestion.setBody(dto.getBody());
        
        if (dto.getTopicTags() != null) {
            Set<Topic> topics = new HashSet<>();
            for (String tag : dto.getTopicTags()) {
                Topic topic = topicRepository.findByName(tag)
                        .orElseGet(() -> topicRepository.save(new Topic(null, tag, new HashSet<>())));

                topics.add(topic);
            }
            dbQuestion.setTopics(topics);
        }

        Question updated = questionRepository.save(dbQuestion);
        return mapper.toResponseDto(updated);
    }

    @Override
    public void deleteQuestion(UUID id) {
        Question question = questionRepository.findActiveById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        question.setDeletedAt(new Date());
        questionRepository.save(question);
    }

}
