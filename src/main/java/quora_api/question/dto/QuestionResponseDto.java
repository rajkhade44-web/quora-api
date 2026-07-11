package quora_api.question.dto;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponseDto {
    private UUID id;
    private UUID userId;
    private String title;
    
    private String body;
    private Date createdAt;
    private List<String> topicNames;
}
