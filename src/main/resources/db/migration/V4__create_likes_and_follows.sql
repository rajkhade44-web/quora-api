-- Likes table (polymorphic)
CREATE TABLE IF NOT EXISTS likes (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    target_type VARCHAR(20) NOT NULL,  -- 'QUESTION', 'ANSWER', 'COMMENT'
    target_id BINARY(16) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    UNIQUE KEY uk_like_unique (user_id, target_type, target_id)
);

-- Follows table
CREATE TABLE IF NOT EXISTS follows (
    id BINARY(16) PRIMARY KEY,
    follower_id BINARY(16) NOT NULL,
    followee_id BINARY(16) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    UNIQUE KEY uk_follow_unique (follower_id, followee_id)
);