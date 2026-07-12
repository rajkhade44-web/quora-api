package quora_api.comment.dto;

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
public class CommentResponseDto {
    private UUID id;
    private UUID userId;
    private String text;          // from body
    private UUID parentId;
    private String parentType;    // "answer" or "comment"
    private Date createdAt;
}
