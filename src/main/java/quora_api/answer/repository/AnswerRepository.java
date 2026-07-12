package quora_api.answer.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import quora_api.answer.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,UUID> {
    @Query("SELECT a FROM Answer a WHERE a.id = :id AND a.deletedAt IS NULL")
    Optional<Answer> findActiveById(@Param("id") UUID id);
}
