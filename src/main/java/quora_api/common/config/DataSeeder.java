package quora_api.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quora_api.question.entity.Question;
import quora_api.question.entity.Topic;
import quora_api.question.repository.QuestionRepository;
import quora_api.question.repository.TopicRepository;
import quora_api.user.entity.User;
import quora_api.user.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final TopicRepository topicRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        if (questionRepository.count() > 500) {
            log.info("Seeder skipped. Data already exists.");
            return;
        }

        List<User> users = seedUsers();

        List<Topic> topics = seedTopics();

        seedQuestions(users, topics);

        log.info("Dataset generation completed successfully.");
    }

    private List<User> seedUsers() {

    if (userRepository.count() >= 100) {
        return userRepository.findAll();
    }

    List<User> users = new ArrayList<>();

    for (int i = 1; i <= 100; i++) {

        User user = User.builder()
                .username("user" + i)
                .email("user" + i + "@mail.com")
                .password(passwordEncoder.encode("Password@123"))
                .bio("Sample bio for user " + i)
                .failedLoginAttempts(0)
                .lockedUntil(null)
                .build();

        users.add(user);
    }

    userRepository.saveAll(users);

    log.info("Inserted {} users", users.size());

    return users;
}
    private List<Topic> seedTopics() {

        List<String> topicNames = List.of(
                "java",
                "spring-boot",
                "hibernate",
                "jpa",
                "mysql",
                "postgresql",
                "redis",
                "docker",
                "kubernetes",
                "aws",
                "jwt",
                "security",
                "oauth2",
                "microservices",
                "kafka",
                "rabbitmq",
                "system-design",
                "react",
                "angular",
                "typescript",
                "javascript",
                "python",
                "golang",
                "dsa",
                "algorithms",
                "backend",
                "frontend",
                "fullstack",
                "rest-api",
                "graphql",
                "testing",
                "junit",
                "mockito",
                "devops",
                "linux",
                "git",
                "github",
                "cloud",
                "azure",
                "gcp",
                "mongodb",
                "elasticsearch",
                "prometheus",
                "grafana",
                "websocket",
                "networking",
                "design-patterns",
                "spring-security",
                "caching",
                "performance"
        );

        List<Topic> topics = new ArrayList<>();

        for (String topicName : topicNames) {

            Topic topic = topicRepository.findByName(topicName)
                    .orElseGet(() ->
                            topicRepository.save(
                                    Topic.builder()
                                            .name(topicName)
                                            .build()
                            )
                    );

            topics.add(topic);
        }

        log.info("Inserted {} topics", topics.size());

        return topics;
    }

    private void seedQuestions(
            List<User> users,
            List<Topic> topics
    ) {

        List<Question> questions = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {

            User randomUser =
                    users.get(
                            ThreadLocalRandom.current()
                                    .nextInt(users.size())
                    );

            Question question = Question.builder()
                    .title("Question " + i)
                    .body("This is sample body content for question " + i)
                    .build();

            question.setUserId(randomUser.getId());

            Collections.shuffle(topics);

            int topicCount =
                    ThreadLocalRandom.current()
                            .nextInt(1, 5);

            topics.stream()
                    .limit(topicCount)
                    .forEach(question::addTopic);

            questions.add(question);
        }

        questionRepository.saveAll(questions);

        log.info("Inserted {} questions", questions.size());
    }
}
