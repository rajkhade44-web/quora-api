package quora_api.question.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class QuestionRequestDto {
    @NotNull
    private UUID userId;

    @NotBlank
    @Size(max = 255, message = "Question topic cannot exceed 255 characters")
    private String title;

    @NotBlank
    private String body;

    private List<String> topicTags; 
}
