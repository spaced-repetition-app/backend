-- =========================
-- Mock data for spaced_repetition
-- =========================

-- ---------- Accounts (5) ----------
INSERT INTO account (id, email, password_hash, is_active, created_by, updated_by)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'alice@example.com',   '$2a$10$alicehash',   TRUE,  NULL, NULL),
    ('22222222-2222-2222-2222-222222222222', 'bob@example.com',     '$2a$10$bobhash',     TRUE,  NULL, NULL),
    ('33333333-3333-3333-3333-333333333333', 'carol@example.com',   '$2a$10$carolhash',   FALSE, NULL, NULL),
    ('44444444-4444-4444-4444-444444444444', 'dave@example.com',    '$2a$10$davehash',    TRUE,  NULL, NULL),
    ('55555555-5555-5555-5555-555555555555', 'eve@example.com',     '$2a$10$evehash',     TRUE,  NULL, NULL);

-- ---------- Users (5) ----------
INSERT INTO `user` (id, account_id, full_name, avatar_url, created_by, updated_by)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','11111111-1111-1111-1111-111111111111','Alice Nguyen','https://pics.example.com/a.png',NULL,NULL),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb','22222222-2222-2222-2222-222222222222','Bob Tran',    NULL,                                NULL,NULL),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc','33333333-3333-3333-3333-333333333333','Carol Pham',  'https://pics.example.com/c.png',    NULL,NULL),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd','44444444-4444-4444-4444-444444444444','Dave Le',     NULL,                                NULL,NULL),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee','55555555-5555-5555-5555-555555555555','Eve Ho',      'https://pics.example.com/e.png',    NULL,NULL);

-- ---------- Login logs (coverage: types, NULLs) ----------
INSERT INTO login_log (id, account_id, login_time, login_type, imei, os, device, created_by, updated_by)
VALUES
    (UUID(), '11111111-1111-1111-1111-111111111111', NOW() - INTERVAL 5 DAY, 'P', '359881234567890', 'iOS',     'iPhone 14', NULL,NULL),
    (UUID(), '11111111-1111-1111-1111-111111111111', NOW() - INTERVAL 2 DAY, 'S', NULL,              'Android', 'Pixel 7',   NULL,NULL),
    (UUID(), '22222222-2222-2222-2222-222222222222', NOW() - INTERVAL 4 DAY, 'P', '358001234567891', NULL,      'Galaxy S22',NULL,NULL),
    (UUID(), '33333333-3333-3333-3333-333333333333', NOW() - INTERVAL 3 DAY, 'S', NULL,              'iOS',     NULL,        NULL,NULL),
    (UUID(), '44444444-4444-4444-4444-444444444444', NOW() - INTERVAL 1 DAY, 'P', '351234567890123', 'Android', 'Xiaomi 13', NULL,NULL),
    (UUID(), '55555555-5555-5555-5555-555555555555', NOW(),                  'S', NULL,              NULL,      'iPad',      NULL,NULL);

-- ---------- Friendships (coverage: PENDING/ACCEPTED/BLOCKED; no self; unique pairs) ----------
INSERT INTO friendship (id, user_id, friend_id, status, created_by, updated_by)
VALUES
    (UUID(),'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb','PENDING', NULL,NULL),
    (UUID(),'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb','cccccccc-cccc-cccc-cccc-cccccccccccc','ACCEPTED',NULL,NULL),
    (UUID(),'cccccccc-cccc-cccc-cccc-cccccccccccc','dddddddd-dddd-dddd-dddd-dddddddddddd','BLOCKED', NULL,NULL),
    (UUID(),'dddddddd-dddd-dddd-dddd-dddddddddddd','eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee','PENDING', NULL,NULL),
    (UUID(),'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa','ACCEPTED',NULL,NULL);

-- ---------- Decks (2 per user = 10 decks) ----------
INSERT INTO deck (id, title, description, owner_id, created_by, updated_by)
VALUES
    ('d1a11111-1111-1111-1111-111111111111','EN Basics','Common English words','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',NULL,NULL),
    ('d1a22222-2222-2222-2222-222222222222','Tech Terms','IT & Dev terms',     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',NULL,NULL),

    ('d2b11111-1111-1111-1111-111111111111','Travel JP','Japanese for travel','bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',NULL,NULL),
    ('d2b22222-2222-2222-2222-222222222222','JLPT N3','N3 vocab','bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',NULL,NULL),

    ('d3c11111-1111-1111-1111-111111111111','EN Business','Business English','cccccccc-cccc-cccc-cccc-cccccccccccc',NULL,NULL),
    ('d3c22222-2222-2222-2222-222222222222','EN Phrasal','Phrasal verbs','cccccccc-cccc-cccc-cccc-cccccccccccc',NULL,NULL),

    ('d4d11111-1111-1111-1111-111111111111','Medical Terms','Med vocab','dddddddd-dddd-dddd-dddd-dddddddddddd',NULL,NULL),
    ('d4d22222-2222-2222-2222-222222222222','Daily EN','Daily use','dddddddd-dddd-dddd-dddd-dddddddddddd',NULL,NULL),

    ('d5e11111-1111-1111-1111-111111111111','Kids EN','For kids','eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',NULL,NULL),
    ('d5e22222-2222-2222-2222-222222222222','Idioms','English idioms','eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',NULL,NULL);

-- ---------- Flashcards (6 per deck = 60 cards; coverage: NEW/LEARNING/REVIEW) ----------
-- For brevity, pattern words; statuses cycle NEW -> LEARNING -> REVIEW
INSERT INTO flashcard (id, front, back, deck_id, status, created_by, updated_by)
VALUES
-- d1a11111
(UUID(),'hello','xin chao','d1a11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'thank you','cam on','d1a11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'please','lam on','d1a11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),
(UUID(),'sorry','xin loi','d1a11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'goodbye','tam biet','d1a11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'morning','buoi sang','d1a11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),

-- d1a22222
(UUID(),'API','giao dien lap trinh','d1a22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'cache','bo nho dem','d1a22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'thread','luong','d1a22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL),
(UUID(),'queue','hang doi','d1a22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'latency','do tre','d1a22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'throughput','luong xu ly','d1a22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL),

-- d2b11111
(UUID(),'eki','nha ga','d2b11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'mise','cua hang','d2b11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'resutoran','nha hang','d2b11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),
(UUID(),'toire','nha ve sinh','d2b11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'mizu','nuoc','d2b11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'kippu','ve tau','d2b11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),

-- d2b22222
(UUID(),'kanji','han tu','d2b22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'bunpo','ngu phap','d2b22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'goi','tu vung','d2b22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL),
(UUID(),'kiku','nghe','d2b22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'yomu','doc','d2b22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'hanasu','noi','d2b22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL),

-- d3c11111
(UUID(),'meeting','cuoc hop','d3c11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'invoice','hoa don','d3c11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'deadline','han chot','d3c11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),
(UUID(),'negotiation','dam phan','d3c11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'proposal','de xuat','d3c11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'budget','ngan sach','d3c11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),

-- d3c22222
(UUID(),'pick up','don','d3c22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'check in','lam thu tuc','d3c22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'look into','xem xet','d3c22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL),
(UUID(),'carry on','tiep tuc','d3c22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'figure out','tim ra','d3c22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'bring up','neu ra','d3c22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL),

-- d4d11111
(UUID(),'diagnosis','chan doan','d4d11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'symptom','trieu chung','d4d11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'treatment','dieu tri','d4d11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),
(UUID(),'vaccine','vac xin','d4d11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'allergy','di ung','d4d11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'dose','lieu dung','d4d11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),

-- d4d22222
(UUID(),'breakfast','bua sang','d4d22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'lunch','bua trua','d4d22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'dinner','bua toi','d4d22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL),
(UUID(),'snack','an nhe','d4d22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'fruit','hoa qua','d4d22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'vegetable','rau cu','d4d22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL),

-- d5e11111
(UUID(),'cat','con meo','d5e11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'dog','con cho','d5e11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'bird','con chim','d5e11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),
(UUID(),'red','mau do','d5e11111-1111-1111-1111-111111111111','NEW',NULL,NULL),
(UUID(),'blue','mau xanh','d5e11111-1111-1111-1111-111111111111','LEARNING',NULL,NULL),
(UUID(),'green','mau xanh la','d5e11111-1111-1111-1111-111111111111','REVIEW',NULL,NULL),

-- d5e22222
(UUID(),'once in a blue moon','hiem khi','d5e22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'break the ice','pha vo kho xu','d5e22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'hit the sack','di ngu','d5e22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL),
(UUID(),'piece of cake','qua de','d5e22222-2222-2222-2222-222222222222','NEW',NULL,NULL),
(UUID(),'under the weather','khong khoe','d5e22222-2222-2222-2222-222222222222','LEARNING',NULL,NULL),
(UUID(),'cost an arm and a leg','rat dat','d5e22222-2222-2222-2222-222222222222','REVIEW',NULL,NULL);

-- ---------- app_function (coverage: is_active TRUE/FALSE, NULLs) ----------
INSERT INTO app_function (id, function_name, function_name_en, icon, version_app, is_active, created_by, updated_by)
VALUES
    ('f0000000-0000-0000-0000-000000000001','hoc_tu_vung','vocab_learning','📚','1.0.0', TRUE,  NULL,NULL),
    ('f0000000-0000-0000-0000-000000000002','on_tap','review','⏰','1.1.0', TRUE,  NULL,NULL),
    ('f0000000-0000-0000-0000-000000000003','bao_cao','reports',NULL,'1.1.1', FALSE, NULL,NULL),
    ('f0000000-0000-0000-0000-000000000004','ban_be','friends','👥',NULL, TRUE,  NULL,NULL);

-- ---------- progress_report (PK = user_id; cover DAY and MONTH) ----------
INSERT INTO progress_report (user_id, total_new, accuracy, streak, period, created_by, updated_by)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 25, 0.86, 7,  'DAY',   NULL,NULL),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 40, 0.73, 12, 'MONTH', NULL,NULL),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 15, 0.92, 3,  'DAY',   NULL,NULL),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 33, 0.65, 9,  'MONTH', NULL,NULL),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 18, 0.80, 5,  'DAY',   NULL,NULL);

-- ---------- review_log (>=100 rows; generate 120 rows using recursive CTE) ----------
-- Uses existing users & flashcards. Cycles result over FORGOTTEN/HARD/GOOD/EASY.
-- next_review_date varies; silent_mode toggles; reaction_time_ms varied.

-- Đảm bảo cho phép đệ quy đủ sâu
SET SESSION cte_max_recursion_depth = 1000;

INSERT INTO review_log
(id, user_id, flashcard_id, result, next_review_date, created_at, updated_at, created_by, updated_by)
WITH RECURSIVE seq(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 120
),
               card_list AS (
                   SELECT ROW_NUMBER() OVER (ORDER BY id) AS rn, id AS flashcard_id
                   FROM flashcard
               ),
               user_list AS (
                   SELECT 1 AS rn, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa' AS user_id UNION ALL
                   SELECT 2,       'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb' UNION ALL
                   SELECT 3,       'cccccccc-cccc-cccc-cccc-cccccccccccc' UNION ALL
                   SELECT 4,       'dddddddd-dddd-dddd-dddd-dddddddddddd' UNION ALL
                   SELECT 5,       'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee'
               )
SELECT
    UUID() AS id,
    u.user_id,
    c.flashcard_id,
    CASE (n % 4)
        WHEN 0 THEN 'FORGOTTEN'
        WHEN 1 THEN 'HARD'
        WHEN 2 THEN 'GOOD'
        ELSE 'EASY'
        END AS result,
    CURDATE() AS next_review_date,                          -- luôn là hôm nay
    NOW() - INTERVAL (n % 15) DAY AS created_at,
    NOW() - INTERVAL (n % 15) DAY AS updated_at,
    NULL AS created_by,
    NULL AS updated_by
FROM seq
    JOIN user_list u
ON ((n - 1) % 5) + 1 = u.rn
    JOIN card_list c
    ON ((n - 1) % 60) + 1 = c.rn;
