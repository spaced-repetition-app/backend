CREATE DATABASE IF NOT EXISTS spaced_repetition CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE spaced_repetition;

--
CREATE TABLE account (
id            CHAR(36)     NOT NULL,
email         VARCHAR(255) NOT NULL,
password_hash VARCHAR(255) NOT NULL,
is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by    CHAR(36)     NULL,
updated_by    CHAR(36)     NULL,
CONSTRAINT pk_account PRIMARY KEY (id),
CONSTRAINT uq_account_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- table user

CREATE TABLE user (
id          CHAR(36)     NOT NULL,
account_id  CHAR(36)     NOT NULL,
full_name   VARCHAR(255) NOT NULL,
avatar_url  VARCHAR(1024) NULL,
created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by  CHAR(36)     NULL,
updated_by  CHAR(36)     NULL,
CONSTRAINT pk_user PRIMARY KEY (id),
CONSTRAINT uq_user_account UNIQUE (account_id),
CONSTRAINT fk_user_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- login log

CREATE TABLE login_log (
id          CHAR(36)    NOT NULL,
account_id  CHAR(36)    NOT NULL,
login_time  DATETIME    NOT NULL,
login_type  CHAR(1)     NOT NULL,           -- ví dụ: 'P' (password), 'S' (social), ...
imei        VARCHAR(36) NULL,
os          VARCHAR(10) NULL,
device      VARCHAR(20) NULL,
created_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by  CHAR(36)    NULL,
updated_by  CHAR(36)    NULL,
CONSTRAINT pk_login_log PRIMARY KEY (id),
CONSTRAINT fk_login_log_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
KEY idx_login_log_account_time (account_id, login_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- friendship

CREATE TABLE friendship (
id          CHAR(36) NOT NULL,
user_id     CHAR(36) NOT NULL,
friend_id   CHAR(36) NOT NULL,
status      ENUM('PENDING','ACCEPTED','BLOCKED') NOT NULL DEFAULT 'PENDING',
created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by  CHAR(36) NULL,
updated_by  CHAR(36) NULL,
CONSTRAINT pk_friendship PRIMARY KEY (id),
CONSTRAINT uq_friend_pair UNIQUE (user_id, friend_id),
CONSTRAINT fk_friendship_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
CONSTRAINT fk_friendship_friend FOREIGN KEY (friend_id) REFERENCES user(id) ON DELETE CASCADE,
CONSTRAINT ck_friend_not_self CHECK (user_id <> friend_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- deck

CREATE TABLE deck (
id          CHAR(36)     NOT NULL,
title       VARCHAR(255) NOT NULL,
description TEXT         NULL,
owner_id    CHAR(36)     NOT NULL,
created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by  CHAR(36)     NULL,
updated_by  CHAR(36)     NULL,
CONSTRAINT pk_deck PRIMARY KEY (id),
CONSTRAINT fk_deck_owner FOREIGN KEY (owner_id) REFERENCES user(id) ON DELETE CASCADE,
KEY idx_deck_owner (owner_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- flashcard;

CREATE TABLE flashcard (
id          CHAR(36) NOT NULL,
front       TEXT     NOT NULL,
back        TEXT     NOT NULL,
deck_id     CHAR(36) NOT NULL,
status      ENUM('NEW','LEARNING','REVIEW') NOT NULL DEFAULT 'NEW',
created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by  CHAR(36) NULL,
updated_by  CHAR(36) NULL,
CONSTRAINT pk_flashcard PRIMARY KEY (id),
CONSTRAINT fk_flashcard_deck FOREIGN KEY (deck_id) REFERENCES deck(id) ON DELETE CASCADE,
KEY idx_flashcard_deck (deck_id),
KEY idx_flashcard_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- review log

CREATE TABLE review_log (
id               CHAR(36) NOT NULL,
user_id          CHAR(36) NOT NULL,
flashcard_id     CHAR(36) NOT NULL,
result           ENUM('FORGOTTEN','HARD','GOOD','EASY') NOT NULL,
next_review_date DATE     NULL,
created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by       CHAR(36) NULL,
updated_by       CHAR(36) NULL,
CONSTRAINT pk_review_log PRIMARY KEY (id),
CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
CONSTRAINT fk_review_flashcard FOREIGN KEY (flashcard_id) REFERENCES flashcard(id) ON DELETE CASCADE,
KEY idx_review_next_date (next_review_date),
KEY idx_review_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- progress_report
CREATE TABLE progress_report (
user_id        CHAR(36) NOT NULL,
total_new      INT      NOT NULL DEFAULT 0,
accuracy       FLOAT    NOT NULL DEFAULT 0,
streak         INT      NOT NULL DEFAULT 0,
period		 ENUM('DAY', 'MONTH') NOT NULL,
created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by     CHAR(36) NULL,
updated_by     CHAR(36) NULL,
CONSTRAINT pk_progress_report PRIMARY KEY (user_id),
CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- app function
CREATE TABLE IF NOT EXISTS app_function (
id               CHAR(36)  NOT NULL,
function_name    CHAR(50)  NOT NULL,
function_name_en CHAR(50)  NULL,
icon             CHAR(10)  NULL,
version_app      CHAR(20)  NULL,
is_active        BOOLEAN   NOT NULL DEFAULT TRUE,
created_at       DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at       DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by       CHAR(36)  NULL,
updated_by       CHAR(36)  NULL,
PRIMARY KEY (id),
UNIQUE KEY uk_app_function_name (function_name)
) ENGINE=InnoDB;

-- review_log
CREATE TABLE IF NOT EXISTS review_log (
id               CHAR(36) NOT NULL,
user_id          CHAR(36) NOT NULL,
flashcard_id     CHAR(36) NOT NULL,
reaction_time_ms INT      NOT NULL,
result           ENUM('AGAIN','HARD','GOOD','EASY') NOT NULL,
silent_mode      BOOLEAN  NOT NULL DEFAULT FALSE,
next_review_date DATE     NULL,
created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by       CHAR(36) NULL,
updated_by       CHAR(36) NULL,
PRIMARY KEY (id),
KEY idx_review_user_card (user_id, flashcard_id),
KEY idx_review_next_date (next_review_date),
CONSTRAINT fk_review_user
FOREIGN KEY (user_id) REFERENCES `user`(id)
ON UPDATE CASCADE
ON DELETE CASCADE,
CONSTRAINT fk_review_flashcard
FOREIGN KEY (flashcard_id) REFERENCES flashcard(id)
ON UPDATE CASCADE
ON DELETE CASCADE
) ENGINE=InnoDB;

