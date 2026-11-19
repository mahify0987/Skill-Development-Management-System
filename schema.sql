-- ====================================================================
-- Database: student_db
-- Schema creation and sample datasets for Student Profile & Skill Development System
-- ====================================================================

CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- Drop dependent tables first
DROP TABLE IF EXISTS student_skills;
DROP TABLE IF EXISTS student;

-- Table: student
-- student_id is manually assigned (NOT AUTO_INCREMENT)
CREATE TABLE student (
    student_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    programming_score DOUBLE NOT NULL,
    mathematics_score DOUBLE NOT NULL,
    PRIMARY KEY (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: student_skills
-- Stores @ElementCollection list of technical skills mapped to student_id
CREATE TABLE student_skills (
    student_id INT NOT NULL,
    skill VARCHAR(100) NOT NULL,
    CONSTRAINT fk_student_skills FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ====================================================================
-- 6 Sample INSERT statements for student
-- ====================================================================
INSERT INTO student (student_id, name, email, course_name, programming_score, mathematics_score) VALUES
(101, 'Aarav Sharma', 'aarav.sharma@kiet.edu', 'Computer Science', 92.0, 88.0),
(102, 'Bhavna Patel', 'bhavna.patel@kiet.edu', 'Information Technology', 45.0, 82.0),
(103, 'Chirag Verma', 'chirag.verma@kiet.edu', 'Computer Science', 74.0, 78.0),
(104, 'Divya Nair', 'divya.nair@kiet.edu', 'Data Science', 85.0, 85.0),
(105, 'Eshan Gupta', 'eshan.gupta@kiet.edu', 'Electronics', 40.0, 48.0),
(106, 'Fatima Khan', 'fatima.khan@kiet.edu', 'Information Technology', 68.0, 62.0);

-- ====================================================================
-- Sample INSERT statements for student_skills
-- Demonstrates skills shared by >= 2 students (Java, Spring Boot, Python, Docker)
-- ====================================================================
INSERT INTO student_skills (student_id, skill) VALUES
-- Student 101: Aarav Sharma (Avg 90.0, Excellent, Stronger: Programming)
(101, 'Java'),
(101, 'Spring Boot'),
(101, 'MySQL'),

-- Student 102: Bhavna Patel (Avg 63.5, Satisfactory, Stronger: Mathematics; Prog < 50 and Math >= 75)
(102, 'Python'),
(102, 'Java'),
(102, 'Docker'),

-- Student 103: Chirag Verma (Avg 76.0, Good, Stronger: Mathematics)
(103, 'Java'),
(103, 'Python'),
(103, 'React'),

-- Student 104: Divya Nair (Avg 85.0, Excellent, Stronger: Equal)
(104, 'Spring Boot'),
(104, 'AWS'),
(104, 'Docker'),

-- Student 105: Eshan Gupta (Avg 44.0, Needs Improvement, Stronger: Mathematics)
(105, 'C++'),
(105, 'Python'),
(105, 'Linux'),

-- Student 106: Fatima Khan (Avg 65.0, Satisfactory, Stronger: Programming)
(106, 'Java'),
(106, 'SQL'),
(106, 'Spring Boot');

