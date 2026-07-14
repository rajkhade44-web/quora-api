-- Create answers table (child of base_content)
CREATE TABLE IF NOT EXISTS answers (
    id BINARY(16) PRIMARY KEY,
    question_id BINARY(16) NOT NULL,
    FOREIGN KEY (id) REFERENCES base_content(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- Create comments table (child of base_content, polymorphic)
CREATE TABLE IF NOT EXISTS comments (
    id BINARY(16) PRIMARY KEY,
    parent_id BINARY(16) NOT NULL,
    parent_type VARCHAR(10) NOT NULL,  -- 'answer' or 'comment'
    FOREIGN KEY (id) REFERENCES base_content(id) ON DELETE CASCADE
);