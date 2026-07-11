package quora_api.question.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import quora_api.common.entity.BaseContent;

@Entity
@Table(name = "questions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@PrimaryKeyJoinColumn(name = "id")
public class Question extends BaseContent {
    @Column(nullable = false)
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "question_topics",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name= "topic_id")
    )
    @Fetch(FetchMode.SUBSELECT) // Resolving N+1 problem
    private Set<Topic> topics = new HashSet<>();

    public void addTopic(Topic topic) {
        topics.add(topic);
        topic.getQuestions().add(this);
    }
    
}
