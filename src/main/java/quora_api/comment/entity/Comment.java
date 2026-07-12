package quora_api.comment.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import quora_api.common.entity.BaseContent;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Comment extends BaseContent {
    @Column(nullable = false)
    private UUID parentId; // Can be Question ID or Answer ID

    @Column(nullable = false)
    private String parentType; // "answer" or "question"
}
