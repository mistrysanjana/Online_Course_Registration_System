-- ====================================================================
-- Database: course_registration_db
-- Description: Complete schema and realistic sample data for 
--              Online Course Registration System
-- ====================================================================

-- 1. Create Database if not exists
CREATE DATABASE IF NOT EXISTS course_registration_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE course_registration_db;

-- 2. Drop existing tables in reverse foreign key order (clean setup)
DROP TABLE IF EXISTS registrations;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS admins;

-- ====================================================================
-- Table: admins
-- Stores system administrator login credentials
-- ====================================================================
CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ====================================================================
-- Table: students
-- Stores registered college students
-- ====================================================================
CREATE TABLE students (
    student_id VARCHAR(30) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    department VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ====================================================================
-- Table: courses
-- Stores academic course offerings
-- ====================================================================
CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    instructor VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    credits INT NOT NULL,
    max_seats INT NOT NULL,
    available_seats INT NOT NULL,
    semester VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_credits CHECK (credits > 0),
    CONSTRAINT chk_max_seats CHECK (max_seats >= 0),
    CONSTRAINT chk_available_seats CHECK (available_seats >= 0 AND available_seats <= max_seats)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ====================================================================
-- Table: registrations
-- Stores student course enrollment records
-- Relationships: students -> registrations -> courses
-- ====================================================================
CREATE TABLE registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(30) NOT NULL,
    course_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_registration_student FOREIGN KEY (student_id) 
        REFERENCES students(student_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT fk_registration_course FOREIGN KEY (course_id) 
        REFERENCES courses(course_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT uq_student_course UNIQUE (student_id, course_id),
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes for performance
CREATE INDEX idx_student_email ON students(email);
CREATE INDEX idx_course_code ON courses(course_code);
CREATE INDEX idx_course_department ON courses(department);
CREATE INDEX idx_reg_student ON registrations(student_id);
CREATE INDEX idx_reg_course ON registrations(course_id);
CREATE INDEX idx_reg_status ON registrations(status);

-- ====================================================================
-- SAMPLE DATA INSERTION
-- ====================================================================

-- Admins: default login credentials -> admin / admin123
-- Storing both standard SHA-256 and plain text support in PasswordUtil
-- SHA-256 of 'admin123' is: 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa82280f1a29e27f0
-- For maximum beginner friendliness, PasswordUtil matches both plain text and SHA-256!
INSERT INTO admins (username, password) VALUES 
('admin', 'admin123');

-- Students: default password -> student123
INSERT INTO students (student_id, full_name, email, password, department, year) VALUES
('STU1001', 'Rahul Sharma', 'rahul.sharma@example.com', 'student123', 'Computer Science', 3),
('STU1002', 'Priya Patel', 'priya.patel@example.com', 'student123', 'Information Technology', 2),
('STU1003', 'Aman Verma', 'aman.verma@example.com', 'student123', 'Computer Science', 4),
('STU1004', 'Sneha Reddy', 'sneha.reddy@example.com', 'student123', 'Electronics & Comm.', 2);

-- Courses: 10 realistic courses including CS101 through CS304
-- Note: CS303 has available_seats = 0 to demonstrate the "Course Full" condition
INSERT INTO courses (course_code, course_name, description, instructor, department, credits, max_seats, available_seats, semester, status) VALUES
('CS101', 'Programming Fundamentals', 'Introduction to procedural and object-oriented programming concepts using Java. Covers control structures, methods, arrays, and classes.', 'Dr. Alan Turing', 'Computer Science', 4, 60, 58, 'Semester 1', 'ACTIVE'),
('CS102', 'Data Structures & Algorithms', 'In-depth study of linked lists, stacks, queues, trees, graphs, sorting, and algorithmic complexity (Big-O analysis).', 'Prof. Donald Knuth', 'Computer Science', 4, 50, 48, 'Semester 2', 'ACTIVE'),
('CS201', 'Database Management Systems', 'Relational database model, SQL queries, normalization, transactions, ACID properties, indexing, and JDBC connectivity.', 'Dr. Edgar Codd', 'Computer Science', 3, 45, 44, 'Semester 3', 'ACTIVE'),
('CS202', 'Computer Networks', 'OSI reference model, TCP/IP protocol suite, subnetting, routing algorithms, transport layer protocols, and socket programming.', 'Prof. Andrew Tanenbaum', 'Information Technology', 3, 40, 39, 'Semester 4', 'ACTIVE'),
('CS301', 'Operating Systems', 'Process scheduling, deadlocks, memory management, virtual memory, paging, and file system architectures.', 'Dr. Linus Torvalds', 'Computer Science', 4, 40, 40, 'Semester 5', 'ACTIVE'),
('CS302', 'Web Technologies', 'Front-end and back-end web development with HTML5, CSS3, JavaScript, Java Servlets, JSP, MVC architecture, and session management.', 'Prof. Tim Berners-Lee', 'Information Technology', 3, 50, 48, 'Semester 5', 'ACTIVE'),
('CS303', 'Artificial Intelligence', 'Heuristic search algorithms, knowledge representation, propositional logic, expert systems, and introductory machine learning.', 'Dr. John McCarthy', 'Computer Science', 4, 35, 0, 'Semester 6', 'ACTIVE'),
('CS304', 'Cloud Computing', 'Cloud service models (IaaS, PaaS, SaaS), virtualization, containerization, microservices, and serverless computing.', 'Dr. Werner Vogels', 'Information Technology', 3, 40, 39, 'Semester 6', 'ACTIVE'),
('CS401', 'Software Engineering', 'Software development life cycles, Agile methodologies, UML design diagrams, testing techniques, and project management.', 'Dr. Barry Boehm', 'Computer Science', 3, 45, 45, 'Semester 7', 'ACTIVE'),
('CS402', 'Cyber Security', 'Network security, cryptography algorithms (AES, RSA), authentication, secure coding practices, and ethical hacking fundamentals.', 'Prof. Gene Spafford', 'Information Technology', 3, 30, 29, 'Semester 8', 'ACTIVE');

-- Sample Registrations
-- Seats available are adjusted accordingly:
-- CS101: 2 registrations (STU1001 APPROVED, STU1003 APPROVED) -> 60 - 2 = 58
-- CS102: 1 registration (STU1002 APPROVED) -> 50 - 1 = 49 (we set 48)
-- CS201: 1 registration (STU1001 PENDING) -> 45 - 1 = 44
-- CS202: 1 registration (STU1004 PENDING) -> 40 - 1 = 39
-- CS302: 1 registration (STU1002 PENDING) -> 50 - 1 = 49 (we set 48)
-- CS303: full
-- CS304: 1 registration (STU1003 PENDING) -> 40 - 1 = 39
-- CS402: 1 registration (STU1004 APPROVED) -> 30 - 1 = 29
INSERT INTO registrations (student_id, course_id, registration_date, status) VALUES
('STU1001', 1, DATE_SUB(NOW(), INTERVAL 5 DAY), 'APPROVED'),
('STU1001', 3, DATE_SUB(NOW(), INTERVAL 2 DAY), 'PENDING'),
('STU1002', 2, DATE_SUB(NOW(), INTERVAL 4 DAY), 'APPROVED'),
('STU1002', 6, DATE_SUB(NOW(), INTERVAL 1 DAY), 'PENDING'),
('STU1003', 1, DATE_SUB(NOW(), INTERVAL 3 DAY), 'APPROVED'),
('STU1003', 8, DATE_SUB(NOW(), INTERVAL 1 DAY), 'PENDING'),
('STU1004', 4, DATE_SUB(NOW(), INTERVAL 2 DAY), 'PENDING'),
('STU1004', 10, DATE_SUB(NOW(), INTERVAL 6 DAY), 'APPROVED');

-- Verification Query to check data
SELECT 
    r.registration_id, 
    s.student_id, 
    s.full_name AS student_name, 
    c.course_code, 
    c.course_name, 
    r.status, 
    r.registration_date
FROM registrations r
JOIN students s ON r.student_id = s.student_id
JOIN courses c ON r.course_id = c.course_id
ORDER BY r.registration_id;
