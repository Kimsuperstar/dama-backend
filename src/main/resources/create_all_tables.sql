-- =============================================
-- Dama Database
-- MariaDB Physical Schema
-- =============================================

CREATE DATABASE IF NOT EXISTS dama
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE dama;


-- =============================================
-- 1. USERS
-- =============================================

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,

    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT uk_users_email
        UNIQUE (email)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;


-- =============================================
-- 2. MOONS
-- =============================================

CREATE TABLE moons (
    id BIGINT NOT NULL AUTO_INCREMENT,

    user_id BIGINT NOT NULL,
    season_year INT NOT NULL,
    share_token VARCHAR(100) NOT NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    -- 공유 링크 토큰은 중복 불가
    CONSTRAINT uk_moons_share_token
        UNIQUE (share_token),

    -- 한 사용자는 한 연도에 하나의 보름달만 생성 가능
    CONSTRAINT uk_moons_user_year
        UNIQUE (user_id, season_year),

    -- 사용자 삭제 시 해당 사용자의 보름달도 삭제
    CONSTRAINT fk_moons_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;


-- =============================================
-- 3. VERSES
-- =============================================

CREATE TABLE verses (
    id BIGINT NOT NULL AUTO_INCREMENT,

    reference VARCHAR(100) NOT NULL,
    text TEXT NOT NULL,

    translation VARCHAR(100) NOT NULL
        DEFAULT '성경전서 개역한글판',

    testament VARCHAR(20) NOT NULL,
    category VARCHAR(30) NOT NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT chk_verses_testament
        CHECK (
            testament IN ('OLD', 'NEW')
        ),

    CONSTRAINT chk_verses_category
        CHECK (
            category IN (
                'THANKS',
                'ENCOURAGE',
                'COMFORT',
                'BLESSING',
                'LOVE',
                'PEACE',
                'NEW_START',
                'FAITH'
            )
        ),

    INDEX idx_verses_category (category)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;


-- =============================================
-- 4. MOON_MESSAGES
-- =============================================

CREATE TABLE moon_messages (
    id BIGINT NOT NULL AUTO_INCREMENT,

    moon_id BIGINT NOT NULL,

    source_type VARCHAR(20) NOT NULL,

    -- PROVIDED인 경우 사용
    verse_id BIGINT NULL,

    -- MANUAL인 경우 사용
    category VARCHAR(30) NULL,
    manual_reference VARCHAR(100) NULL,
    manual_text TEXT NULL,

    personal_message VARCHAR(500) NULL,

    -- 익명일 경우 NULL 가능
    author_name VARCHAR(50) NULL,

    is_anonymous BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT chk_moon_messages_source_type
        CHECK (
            source_type IN ('PROVIDED', 'MANUAL')
        ),

    CONSTRAINT chk_moon_messages_category
        CHECK (
            category IS NULL
            OR category IN (
                'THANKS',
                'ENCOURAGE',
                'COMFORT',
                'BLESSING',
                'LOVE',
                'PEACE',
                'NEW_START',
                'FAITH'
            )
        ),

    -- PROVIDED / MANUAL 데이터 규칙
    CONSTRAINT chk_moon_messages_source_data
        CHECK (
            (
                source_type = 'PROVIDED'
                AND verse_id IS NOT NULL
                AND category IS NULL
                AND manual_reference IS NULL
                AND manual_text IS NULL
            )
            OR
            (
                source_type = 'MANUAL'
                AND verse_id IS NULL
                AND manual_reference IS NOT NULL
                AND manual_text IS NOT NULL
            )
        ),

    INDEX idx_moon_messages_moon_id (moon_id),
    INDEX idx_moon_messages_verse_id (verse_id),

    -- 보름달 삭제 시 해당 메시지도 같이 삭제
    CONSTRAINT fk_moon_messages_moon
        FOREIGN KEY (moon_id)
        REFERENCES moons(id)
        ON DELETE CASCADE,

    -- 사용 중인 제공 말씀은 임의 삭제 방지
	     CONSTRAINT fk_moon_messages_verse
        FOREIGN KEY (verse_id)
        REFERENCES verses(id)
        ON DELETE RESTRICT
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;