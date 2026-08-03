package quora_api.like.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import quora_api.answer.repository.AnswerRepository;
import quora_api.common.enums.LikeTargetType;
import quora_api.common.exception.ResourceNotFoundException;
import quora_api.like.entity.Like;
import quora_api.like.repository.LikeRepository;
import quora_api.like.service.LikeService;
import quora_api.question.repository.QuestionRepository;
import quora_api.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;


    @Override
    public void like(UUID targetId, String targetType, UUID currentUserId) {

        if (!userRepository.existsById(currentUserId)) {
            throw new ResourceNotFoundException("User not found");
        }

        LikeTargetType type;
        try{
            type = LikeTargetType.valueOf(targetType.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid target type: " + targetType);
        }

        boolean targetExists = switch(type){
            case QUESTION -> questionRepository.existsById(targetId);
            case ANSWER -> answerRepository.existsById(targetId);
            case COMMENT -> questionRepository.existsById(targetId);
        };

        if (!targetExists) {
            throw new ResourceNotFoundException(type.name() + " with id " + targetId.toString() + " not found");
        }

        if (likeRepository.existsByUserIdAndTargetTypeAndTargetId(currentUserId, type, targetId)) {
            return;
        }

        Like like = Like.builder()
            .targetId(targetId)
            .targetType(type)
            .userId(currentUserId)
            .build();
        likeRepository.save(like);

    }

   
    

}
