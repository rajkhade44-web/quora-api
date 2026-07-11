-- Create base_content table (parent for questions, answers, comments)
CREATE TABLE IF NOT EXISTS base_content (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL
);

-- Create questions (child of base_content)
CREATE TABLE IF NOT EXISTS questions (
    id BINARY(16) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES base_content(id) ON DELETE CASCADE
);

-- Create topics
CREATE TABLE IF NOT EXISTS topics (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create join table for many-to-many
CREATE TABLE IF NOT EXISTS question_topics (
    question_id BINARY(16) NOT NULL,
    topic_id BINARY(16) NOT NULL,
    PRIMARY KEY (question_id, topic_id),
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE
);