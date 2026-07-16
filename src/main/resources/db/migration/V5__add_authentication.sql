-- Add password and lockout fields to users
ALTER TABLE users
    ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT 'temporary',
    ADD COLUMN failed_login_attempts INT DEFAULT 0,
    ADD COLUMN locked_until TIMESTAMP NULL;

-- Refresh tokens table
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    family_id BINARY(16) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Index for fast lookup by token
CREATE INDEX idx_refresh_token ON refresh_tokens(token);