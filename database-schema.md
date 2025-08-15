# Database Schema - Spaced Repetition System

## Overview
Complete database schema for the Spaced Repetition flashcard management system. This schema supports the MVP version without authentication, focusing on core functionality.

## Schema Design Principles
- **UUID Primary Keys**: All entities use UUID for better distribution and security
- **Soft Delete**: Important entities support soft deletion with `deleted_at` timestamp
- **Audit Fields**: All entities inherit `created_at` and `updated_at` from BaseEntity
- **Referential Integrity**: Foreign keys with appropriate constraints
- **Indexing Strategy**: Optimized indexes for common query patterns

---

## 1. Core Entities

### 1.1 Decks Table
**Purpose**: Container for organizing flashcards by topic/subject

```sql
CREATE TABLE decks (
    -- Primary Key
    id VARCHAR(36) NOT NULL PRIMARY KEY DEFAULT (UUID()),
    
    -- Basic Information
    title VARCHAR(255) NOT NULL,
    description TEXT NULL,
    
    -- Metadata
    total_cards INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    difficulty_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') DEFAULT 'BEGINNER',
    
    -- Audit Fields (from BaseEntity)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    
    -- Constraints
    CONSTRAINT chk_deck_title_length CHECK (CHAR_LENGTH(title) BETWEEN 1 AND 255),
    CONSTRAINT chk_deck_total_cards CHECK (total_cards >= 0)
);

-- Indexes
CREATE INDEX idx_decks_title ON decks(title);
CREATE INDEX idx_decks_created_at ON decks(created_at);
CREATE INDEX idx_decks_active ON decks(is_active, deleted_at);
```

### 1.2 Flashcards Table
**Purpose**: Individual flashcard items within decks

```sql
CREATE TABLE flashcards (
    -- Primary Key
    id VARCHAR(36) NOT NULL PRIMARY KEY DEFAULT (UUID()),
    
    -- Content
    front TEXT NOT NULL,
    back TEXT NOT NULL,
    hint TEXT NULL,
    
    -- Relationships
    deck_id VARCHAR(36) NOT NULL,
    
    -- Status & Learning Progress
    status ENUM('NEW', 'LEARNING', 'REVIEW', 'MASTERED') DEFAULT 'NEW',
    difficulty_rating DECIMAL(3,2) DEFAULT 2.50, -- 1.00 to 5.00
    interval_days INT DEFAULT 1,
    repetitions INT DEFAULT 0,
    ease_factor DECIMAL(3,2) DEFAULT 2.50,
    
    -- Next review scheduling
    next_review_date DATETIME NULL,
    last_reviewed_at DATETIME NULL,
    
    -- Audit Fields
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    
    -- Foreign Key Constraints
    CONSTRAINT fk_flashcards_deck_id 
        FOREIGN KEY (deck_id) REFERENCES decks(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    
    -- Data Constraints
    CONSTRAINT chk_flashcard_content_not_empty CHECK (
        CHAR_LENGTH(TRIM(front)) > 0 AND CHAR_LENGTH(TRIM(back)) > 0
    ),
    CONSTRAINT chk_flashcard_difficulty CHECK (difficulty_rating BETWEEN 1.00 AND 5.00),
    CONSTRAINT chk_flashcard_ease_factor CHECK (ease_factor BETWEEN 1.30 AND 5.00),
    CONSTRAINT chk_flashcard_repetitions CHECK (repetitions >= 0),
    CONSTRAINT chk_flashcard_interval CHECK (interval_days >= 1)
);

-- Indexes
CREATE INDEX idx_flashcards_deck_id ON flashcards(deck_id);
CREATE INDEX idx_flashcards_status ON flashcards(status);
CREATE INDEX idx_flashcards_next_review ON flashcards(next_review_date);
CREATE INDEX idx_flashcards_created_at ON flashcards(created_at);
CREATE INDEX idx_flashcards_deck_status ON flashcards(deck_id, status);
```

---

## 2. Study Session Management

### 2.1 Study Sessions Table
**Purpose**: Track individual study sessions for progress monitoring

```sql
CREATE TABLE study_sessions (
    -- Primary Key
    id VARCHAR(36) NOT NULL PRIMARY KEY DEFAULT (UUID()),
    
    -- Session Information
    deck_id VARCHAR(36) NOT NULL,
    session_name VARCHAR(100) NULL,
    
    -- Session State
    status ENUM('ACTIVE', 'PAUSED', 'COMPLETED', 'ABANDONED') DEFAULT 'ACTIVE',
    current_card_index INT DEFAULT 0,
    total_cards_planned INT NOT NULL,
    
    -- Progress Tracking
    cards_studied INT DEFAULT 0,
    cards_correct INT DEFAULT 0,
    cards_incorrect INT DEFAULT 0,
    
    -- Timing
    started_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    ended_at DATETIME NULL,
    total_duration_seconds INT DEFAULT 0,
    
    -- Session Settings
    study_mode ENUM('SEQUENTIAL', 'RANDOM', 'SPACED_REPETITION') DEFAULT 'SEQUENTIAL',
    show_hints BOOLEAN DEFAULT TRUE,
    auto_advance BOOLEAN DEFAULT FALSE,
    
    -- Audit Fields
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Key Constraints
    CONSTRAINT fk_study_sessions_deck_id 
        FOREIGN KEY (deck_id) REFERENCES decks(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    
    -- Data Constraints
    CONSTRAINT chk_session_progress CHECK (
        cards_studied = (cards_correct + cards_incorrect) AND
        cards_studied <= total_cards_planned
    ),
    CONSTRAINT chk_session_duration CHECK (total_duration_seconds >= 0)
);

-- Indexes
CREATE INDEX idx_study_sessions_deck_id ON study_sessions(deck_id);
CREATE INDEX idx_study_sessions_status ON study_sessions(status);
CREATE INDEX idx_study_sessions_started_at ON study_sessions(started_at);
```

---

## 3. Learning Progress Tracking

### 3.1 Review Logs Table (Enhanced)
**Purpose**: Detailed logging of each flashcard review for analytics and spaced repetition

```sql
CREATE TABLE review_logs (
    -- Primary Key
    id VARCHAR(36) NOT NULL PRIMARY KEY DEFAULT (UUID()),
    
    -- Relationships
    study_session_id VARCHAR(36) NOT NULL,
    flashcard_id VARCHAR(36) NOT NULL,
    
    -- Review Details
    review_result ENUM('AGAIN', 'HARD', 'GOOD', 'EASY') NOT NULL,
    reaction_time_ms INT NULL,
    
    -- Learning Metrics
    previous_ease_factor DECIMAL(3,2) NULL,
    new_ease_factor DECIMAL(3,2) NULL,
    previous_interval_days INT NULL,
    new_interval_days INT NULL,
    
    -- Context
    was_hint_used BOOLEAN DEFAULT FALSE,
    silent_mode BOOLEAN DEFAULT FALSE,
    review_type ENUM('FIRST_TIME', 'REVIEW', 'RELEARN') DEFAULT 'FIRST_TIME',
    
    -- Scheduling
    next_review_date DATETIME NULL,
    
    -- Audit Fields
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Key Constraints
    CONSTRAINT fk_review_logs_session_id 
        FOREIGN KEY (study_session_id) REFERENCES study_sessions(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_review_logs_flashcard_id 
        FOREIGN KEY (flashcard_id) REFERENCES flashcards(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    
    -- Data Constraints
    CONSTRAINT chk_review_logs_reaction_time CHECK (reaction_time_ms >= 0),
    CONSTRAINT chk_review_logs_ease_factor CHECK (
        (previous_ease_factor IS NULL OR previous_ease_factor BETWEEN 1.30 AND 5.00) AND
        (new_ease_factor IS NULL OR new_ease_factor BETWEEN 1.30 AND 5.00)
    )
);

-- Indexes
CREATE INDEX idx_review_logs_session_id ON review_logs(study_session_id);
CREATE INDEX idx_review_logs_flashcard_id ON review_logs(flashcard_id);
CREATE INDEX idx_review_logs_created_at ON review_logs(created_at);
CREATE INDEX idx_review_logs_result ON review_logs(review_result);
CREATE INDEX idx_review_logs_next_review ON review_logs(next_review_date);
```

---

## 4. Analytics & Statistics

### 4.1 Deck Statistics Table
**Purpose**: Aggregated statistics for decks (can be computed or cached)

```sql
CREATE TABLE deck_statistics (
    -- Primary Key
    id VARCHAR(36) NOT NULL PRIMARY KEY DEFAULT (UUID()),
    
    -- Relationships
    deck_id VARCHAR(36) NOT NULL UNIQUE,
    
    -- Card Distribution
    total_cards INT DEFAULT 0,
    new_cards INT DEFAULT 0,
    learning_cards INT DEFAULT 0,
    review_cards INT DEFAULT 0,
    mastered_cards INT DEFAULT 0,
    
    -- Study Metrics
    total_study_sessions INT DEFAULT 0,
    total_study_time_minutes INT DEFAULT 0,
    average_session_time_minutes DECIMAL(8,2) DEFAULT 0,
    
    -- Performance Metrics
    total_reviews INT DEFAULT 0,
    correct_reviews INT DEFAULT 0,
    accuracy_percentage DECIMAL(5,2) DEFAULT 0,
    average_reaction_time_ms INT DEFAULT 0,
    
    -- Learning Progress
    cards_due_today INT DEFAULT 0,
    cards_overdue INT DEFAULT 0,
    estimated_completion_days INT DEFAULT 0,
    
    -- Timestamps
    last_calculated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Key Constraints
    CONSTRAINT fk_deck_statistics_deck_id 
        FOREIGN KEY (deck_id) REFERENCES decks(id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    
    -- Data Constraints
    CONSTRAINT chk_deck_stats_totals CHECK (
        total_cards = (new_cards + learning_cards + review_cards + mastered_cards)
    ),
    CONSTRAINT chk_deck_stats_accuracy CHECK (accuracy_percentage BETWEEN 0 AND 100)
);

-- Indexes
CREATE INDEX idx_deck_statistics_deck_id ON deck_statistics(deck_id);
CREATE INDEX idx_deck_statistics_last_calculated ON deck_statistics(last_calculated_at);
```

---

## 5. System Configuration

### 5.1 System Settings Table
**Purpose**: Global system configuration and feature flags

```sql
CREATE TABLE system_settings (
    -- Primary Key
    id VARCHAR(36) NOT NULL PRIMARY KEY DEFAULT (UUID()),
    
    -- Setting Details
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT NOT NULL,
    setting_type ENUM('STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'JSON') DEFAULT 'STRING',
    
    -- Metadata
    description TEXT NULL,
    category VARCHAR(50) DEFAULT 'GENERAL',
    is_public BOOLEAN DEFAULT FALSE,
    
    -- Audit Fields
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_setting_key_format CHECK (
        setting_key REGEXP '^[A-Z][A-Z0-9_]*[A-Z0-9]$'
    )
);

-- Indexes
CREATE INDEX idx_system_settings_key ON system_settings(setting_key);
CREATE INDEX idx_system_settings_category ON system_settings(category);

-- Default Settings
INSERT INTO system_settings (setting_key, setting_value, setting_type, description, category) VALUES
('MAX_DECKS_PER_USER', '50', 'INTEGER', 'Maximum number of decks per user', 'LIMITS'),
('MAX_CARDS_PER_DECK', '1000', 'INTEGER', 'Maximum number of cards per deck', 'LIMITS'),
('DEFAULT_STUDY_SESSION_SIZE', '20', 'INTEGER', 'Default number of cards in study session', 'STUDY'),
('SPACED_REPETITION_ENABLED', 'true', 'BOOLEAN', 'Enable spaced repetition algorithm', 'FEATURES'),
('AUTO_ADVANCE_CARDS', 'false', 'BOOLEAN', 'Auto advance to next card after answer', 'STUDY');
```

---

## 6. Triggers & Stored Procedures

### 6.1 Automatic Statistics Update Trigger

```sql
DELIMITER //

-- Update deck statistics when flashcards change
CREATE TRIGGER update_deck_stats_on_flashcard_change
    AFTER INSERT ON flashcards
    FOR EACH ROW
BEGIN
    INSERT INTO deck_statistics (deck_id, total_cards, new_cards, last_calculated_at)
    VALUES (NEW.deck_id, 1, 1, NOW())
    ON DUPLICATE KEY UPDATE
        total_cards = total_cards + 1,
        new_cards = new_cards + 1,
        last_calculated_at = NOW();
END //

-- Update deck total_cards count
CREATE TRIGGER update_deck_total_cards
    AFTER INSERT ON flashcards
    FOR EACH ROW
BEGIN
    UPDATE decks 
    SET total_cards = total_cards + 1,
        updated_at = NOW()
    WHERE id = NEW.deck_id;
END //

DELIMITER ;
```

### 6.2 Stored Procedure for Calculating Due Cards

```sql
DELIMITER //

CREATE PROCEDURE CalculateDueCards(IN deck_id_param VARCHAR(36))
BEGIN
    DECLARE due_count INT DEFAULT 0;
    
    SELECT COUNT(*) INTO due_count
    FROM flashcards
    WHERE deck_id = deck_id_param
      AND deleted_at IS NULL
      AND (next_review_date IS NULL OR next_review_date <= NOW())
      AND status IN ('NEW', 'LEARNING', 'REVIEW');
    
    UPDATE deck_statistics
    SET cards_due_today = due_count,
        last_calculated_at = NOW()
    WHERE deck_id = deck_id_param;
END //

DELIMITER ;
```

---

## 7. Views for Common Queries

### 7.1 Active Decks with Statistics View

```sql
CREATE VIEW active_decks_with_stats AS
SELECT 
    d.id,
    d.title,
    d.description,
    d.difficulty_level,
    d.total_cards,
    d.created_at,
    ds.new_cards,
    ds.learning_cards,
    ds.review_cards,
    ds.mastered_cards,
    ds.cards_due_today,
    ds.accuracy_percentage,
    ds.total_study_sessions,
    ds.total_study_time_minutes
FROM decks d
LEFT JOIN deck_statistics ds ON d.id = ds.deck_id
WHERE d.deleted_at IS NULL
  AND d.is_active = TRUE
ORDER BY d.created_at DESC;
```

### 7.2 Study Session Progress View

```sql
CREATE VIEW study_session_progress AS
SELECT 
    ss.id as session_id,
    ss.status,
    ss.started_at,
    ss.ended_at,
    ss.total_duration_seconds,
    ss.cards_studied,
    ss.cards_correct,
    ss.cards_incorrect,
    d.title as deck_title,
    d.total_cards as deck_total_cards,
    ROUND((ss.cards_studied / ss.total_cards_planned) * 100, 2) as progress_percentage,
    CASE 
        WHEN ss.cards_studied > 0 THEN ROUND((ss.cards_correct / ss.cards_studied) * 100, 2)
        ELSE 0 
    END as accuracy_percentage
FROM study_sessions ss
JOIN decks d ON ss.deck_id = d.id
WHERE d.deleted_at IS NULL;
```

---

## 8. Data Migration Scripts

### 8.1 Initial Data Setup

```sql
-- Create initial system settings
INSERT INTO system_settings (setting_key, setting_value, setting_type, description, category) VALUES
('MAINTENANCE_MODE', 'false', 'BOOLEAN', 'System maintenance mode', 'SYSTEM'),
('API_VERSION', '1.0.0', 'STRING', 'Current API version', 'SYSTEM'),
('MAX_SESSION_DURATION_HOURS', '4', 'INTEGER', 'Maximum study session duration in hours', 'STUDY');

-- Create sample deck for testing
INSERT INTO decks (id, title, description, difficulty_level) VALUES
(UUID(), 'Sample Spanish Vocabulary', 'Basic Spanish words for beginners', 'BEGINNER');

-- Get the deck ID for sample flashcards
SET @sample_deck_id = (SELECT id FROM decks WHERE title = 'Sample Spanish Vocabulary' LIMIT 1);

-- Create sample flashcards
INSERT INTO flashcards (deck_id, front, back) VALUES
(@sample_deck_id, 'Hello', 'Hola'),
(@sample_deck_id, 'Goodbye', 'Adiós'),
(@sample_deck_id, 'Thank you', 'Gracias'),
(@sample_deck_id, 'Please', 'Por favor'),
(@sample_deck_id, 'Yes', 'Sí'),
(@sample_deck_id, 'No', 'No');
```

---

## 9. Performance Optimization

### 9.1 Recommended Indexes

```sql
-- Composite indexes for common query patterns
CREATE INDEX idx_flashcards_deck_status_review ON flashcards(deck_id, status, next_review_date);
CREATE INDEX idx_review_logs_flashcard_created ON review_logs(flashcard_id, created_at);
CREATE INDEX idx_study_sessions_deck_started ON study_sessions(deck_id, started_at);

-- Full-text search indexes
CREATE FULLTEXT INDEX idx_decks_title_description ON decks(title, description);
CREATE FULLTEXT INDEX idx_flashcards_content ON flashcards(front, back, hint);
```

### 9.2 Partitioning Strategy (for large datasets)

```sql
-- Partition review_logs by month for better performance
ALTER TABLE review_logs PARTITION BY RANGE (TO_DAYS(created_at)) (
    PARTITION p202501 VALUES LESS THAN (TO_DAYS('2025-02-01')),
    PARTITION p202502 VALUES LESS THAN (TO_DAYS('2025-03-01')),
    PARTITION p202503 VALUES LESS THAN (TO_DAYS('2025-04-01')),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);
```

---

## 10. Backup and Maintenance

### 10.1 Regular Maintenance Tasks

```sql
-- Clean up old deleted records (older than 30 days)
DELETE FROM decks WHERE deleted_at < DATE_SUB(NOW(), INTERVAL 30 DAY);
DELETE FROM flashcards WHERE deleted_at < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- Update statistics for all decks
UPDATE deck_statistics ds
JOIN (
    SELECT 
        deck_id,
        COUNT(*) as total,
        SUM(CASE WHEN status = 'NEW' THEN 1 ELSE 0 END) as new_count,
        SUM(CASE WHEN status = 'LEARNING' THEN 1 ELSE 0 END) as learning_count,
        SUM(CASE WHEN status = 'REVIEW' THEN 1 ELSE 0 END) as review_count,
        SUM(CASE WHEN status = 'MASTERED' THEN 1 ELSE 0 END) as mastered_count
    FROM flashcards 
    WHERE deleted_at IS NULL 
    GROUP BY deck_id
) f ON ds.deck_id = f.deck_id
SET 
    ds.total_cards = f.total,
    ds.new_cards = f.new_count,
    ds.learning_cards = f.learning_count,
    ds.review_cards = f.review_count,
    ds.mastered_cards = f.mastered_count,
    ds.last_calculated_at = NOW();
```

---

*📊 Database Schema Version 1.0 - Created: 2025-08-13*
*🔄 Supports MVP functionality with future extensibility*