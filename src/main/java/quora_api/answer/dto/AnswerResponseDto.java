package quora_api.answer.dto;

import java.util.Date;
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
public class AnswerResponseDto {
    private UUID id;
    private UUID userId;
    private UUID questionId;
    private String text;
    private Date createdAt;
}
