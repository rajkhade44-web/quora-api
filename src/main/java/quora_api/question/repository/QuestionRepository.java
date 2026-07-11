package quora_api.question.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quora_api.question.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    @Query("SELECT q FROM Question q WHERE q.deletedAt IS NULL")
    Page<Question> findAllWithTopics(Pageable pageable);

    @Query(
        "SELECT DISTINCT q FROM Question q LEFT JOIN q.topics t " +
           "WHERE (:text IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :text, '%')) " +
           "OR LOWER(q.body) LIKE LOWER(CONCAT('%', :text, '%'))) " +
           "AND (:tag IS NULL OR t.name = :tag) " +
           "AND q.deletedAt IS NULL"
    )
    Page<Question> searchWithTopics(@Param("text") String text, @Param("tag") String tag, Pageable pageable);
}
