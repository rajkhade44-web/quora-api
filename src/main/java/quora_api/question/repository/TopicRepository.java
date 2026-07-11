package quora_api.question.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import quora_api.question.entity.Topic;
import java.util.List;


@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID>{
    Optional<Topic> findByName(String name);
}
