-- ============================================================
--  NEXUS CUP 2025 — Esports Management System
--  MySQL Schema & Seed Data
--  Run this entire file in MySQL Workbench ONCE before launch
-- ============================================================

-- 1. Create and select the database
CREATE DATABASE IF NOT EXISTS esports_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE esports_db;

-- ============================================================
--  DROP existing tables (safe re-run)
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS team_tournament;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS tournaments;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
--  TABLE: users
-- ============================================================
CREATE TABLE users (
    id          VARCHAR(20)  PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    full_name   VARCHAR(100) NOT NULL,
    role        ENUM('ADMIN','MANAGER') NOT NULL
);

-- ============================================================
--  TABLE: teams
-- ============================================================
CREATE TABLE teams (
    id          VARCHAR(20)  PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    manager_id  VARCHAR(20)  NOT NULL,
    registered  TINYINT(1)   NOT NULL DEFAULT 0,
    FOREIGN KEY (manager_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================
--  TABLE: players
-- ============================================================
CREATE TABLE players (
    id          VARCHAR(20)  PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    ign         VARCHAR(100) NOT NULL,
    role        VARCHAR(50)  NOT NULL,
    team_id     VARCHAR(20)  NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE
);

-- ============================================================
--  TABLE: tournaments
-- ============================================================
CREATE TABLE tournaments (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL
);

-- ============================================================
--  TABLE: matches
-- ============================================================
CREATE TABLE matches (
    id           VARCHAR(20)  PRIMARY KEY,
    tournament_id INT         NOT NULL,
    team1_id     VARCHAR(20)  NOT NULL,
    team2_id     VARCHAR(20)  NOT NULL,
    score1       INT          NOT NULL DEFAULT 0,
    score2       INT          NOT NULL DEFAULT 0,
    status       ENUM('UPCOMING','ONGOING','FINISHED') NOT NULL DEFAULT 'UPCOMING',
    winner_id    VARCHAR(20)  NULL,
    round        ENUM('SEMIFINAL','FINAL') NOT NULL,
    match_number INT          NOT NULL DEFAULT 1,
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY (team1_id)      REFERENCES teams(id),
    FOREIGN KEY (team2_id)      REFERENCES teams(id),
    FOREIGN KEY (winner_id)     REFERENCES teams(id)
);

-- ============================================================
--  SEED DATA
-- ============================================================

-- Admin account
INSERT INTO users VALUES ('A001', 'admin', 'admin123', 'System Admin', 'ADMIN');

-- Managers
INSERT INTO users VALUES
    ('M001', 'manager1', 'pass123', 'Alex Chen',   'MANAGER'),
    ('M002', 'manager2', 'pass123', 'Sarah Kim',   'MANAGER'),
    ('M003', 'manager3', 'pass123', 'Mike Torres', 'MANAGER'),
    ('M004', 'manager4', 'pass123', 'Lisa Park',   'MANAGER');

-- Teams
INSERT INTO teams VALUES
    ('T001', 'Dragon Esports', 'M001', 1),
    ('T002', 'Phoenix Rising', 'M002', 1),
    ('T003', 'Storm Wolves',   'M003', 1),
    ('T004', 'Iron Titans',    'M004', 1);

-- Dragon Esports roster
INSERT INTO players VALUES
    ('PT001_0', 'James Liu',   'DragonKing',  'Top',       'T001'),
    ('PT001_1', 'Kevin Park',  'JungleFire',  'Jungle',    'T001'),
    ('PT001_2', 'Tommy Chen',  'MidNova',     'Mid',       'T001'),
    ('PT001_3', 'Ryan Wu',     'BotSlayer',   'Bot Carry', 'T001'),
    ('PT001_4', 'Chris Lee',   'SupportGod',  'Support',   'T001'),
    ('PT001_5', 'Daniel Kim',  'SixthSense',  'Sixth Man', 'T001');

-- Phoenix Rising roster
INSERT INTO players VALUES
    ('PT002_0', 'Marcus Bell', 'PhoenixTop',  'Top',       'T002'),
    ('PT002_1', 'Jake Torres', 'StormJung',   'Jungle',    'T002'),
    ('PT002_2', 'Ethan Fox',   'MidFlame',    'Mid',       'T002'),
    ('PT002_3', 'Noah King',   'BotPhoenix',  'Bot Carry', 'T002'),
    ('PT002_4', 'Liam Ross',   'SupportRise', 'Support',   'T002'),
    ('PT002_5', 'Aiden Cole',  'SixthFlame',  'Sixth Man', 'T002');

-- Storm Wolves roster
INSERT INTO players VALUES
    ('PT003_0', 'Victor Gray', 'WolfTop',     'Top',       'T003'),
    ('PT003_1', 'Oscar Hunt',  'StormHunt',   'Jungle',    'T003'),
    ('PT003_2', 'Felix Stone', 'MidWolf',     'Mid',       'T003'),
    ('PT003_3', 'Lucas Ward',  'BotStorm',    'Bot Carry', 'T003'),
    ('PT003_4', 'Mason Price', 'SupportWolf', 'Support',   'T003'),
    ('PT003_5', 'Eli Turner',  'SixthWolf',   'Sixth Man', 'T003');

-- Iron Titans roster
INSERT INTO players VALUES
    ('PT004_0', 'Ivan Stark',  'IronWall',     'Top',       'T004'),
    ('PT004_1', 'Hugo Blake',  'TitanJung',    'Jungle',    'T004'),
    ('PT004_2', 'Finn Cross',  'MidTitan',     'Mid',       'T004'),
    ('PT004_3', 'Ezra Lane',   'BotIron',      'Bot Carry', 'T004'),
    ('PT004_4', 'Rory Stone',  'SupportTitan', 'Support',   'T004'),
    ('PT004_5', 'Cole Reed',   'SixthTitan',   'Sixth Man', 'T004');

-- Tournament
INSERT INTO tournaments VALUES (1, 'NEXUS CUP 2025');

-- Matches (SF1 and SF2 are finished, Final is upcoming)
INSERT INTO matches VALUES
    ('SF1', 1, 'T001', 'T003', 3, 2, 'FINISHED', 'T001', 'SEMIFINAL', 1),
    ('SF2', 1, 'T002', 'T004', 3, 1, 'FINISHED', 'T002', 'SEMIFINAL', 2),
    ('F1',  1, 'T001', 'T002', 0, 0, 'UPCOMING', NULL,   'FINAL',     3);

-- ============================================================
--  Verify data
-- ============================================================
SELECT 'Users:'    AS '', COUNT(*) FROM users;
SELECT 'Teams:'    AS '', COUNT(*) FROM teams;
SELECT 'Players:'  AS '', COUNT(*) FROM players;
SELECT 'Matches:'  AS '', COUNT(*) FROM matches;
