-- Campus Connect Database Schema
-- MySQL Database Setup Script

-- Create database
CREATE DATABASE IF NOT EXISTS campus_connect;
USE campus_connect;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS queries;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS profiles;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('teacher', 'student') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create posts table
CREATE TABLE posts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create events table
CREATE TABLE events (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    event_date DATE NOT NULL,
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Create queries table
CREATE TABLE queries (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NULL,
    answered_by INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    answered_at TIMESTAMP NULL,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (answered_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Create profiles table (optional extended user info)
CREATE TABLE profiles (
    user_id INT PRIMARY KEY,
    class VARCHAR(20),
    section VARCHAR(20),
    bio TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert sample data
-- Sample users
INSERT INTO users (name, email, password, role) VALUES
('Dr. Sarah Johnson', 'teacher@demo.com', 'teacher123', 'teacher'),
('Alex Smith', 'student@demo.com', 'student123', 'student'),
('Prof. Michael Brown', 'michael.brown@university.edu', 'prof123', 'teacher'),
('Emma Wilson', 'emma.wilson@student.edu', 'emma123', 'student'),
('John Davis', 'john.davis@student.edu', 'john123', 'student');

-- Sample posts
INSERT INTO posts (user_id, content) VALUES
(1, 'Welcome to the new semester! Looking forward to working with all of you. 📚'),
(2, 'Excited to start my Computer Science journey this year! Any tips from seniors?'),
(3, 'Don''t forget about the upcoming hackathon registration deadline. Great opportunity to showcase your skills!'),
(4, 'Just finished my first project in Java. The learning curve is steep but rewarding!'),
(1, 'Office hours this week: Tuesday and Thursday 2-4 PM. Feel free to drop by with any questions.');

-- Sample events
INSERT INTO events (title, description, event_date, created_by) VALUES
('Annual Tech Hackathon 2024', 'Join us for a 48-hour coding marathon! Build innovative solutions and compete for amazing prizes. Open to all students with basic programming knowledge.', '2024-03-15', 1),
('AI Workshop Series', 'Learn the fundamentals of Artificial Intelligence and Machine Learning. This 3-day workshop covers Python, TensorFlow, and practical AI applications.', '2024-02-28', 3),
('Career Fair 2024', 'Meet with top tech companies and explore internship and job opportunities. Bring your resumes and dress professionally!', '2024-04-10', 1),
('Open Source Contribution Drive', 'Learn how to contribute to open source projects. We''ll guide you through Git, GitHub, and making your first contribution.', '2024-03-01', 3);

-- Sample queries
INSERT INTO queries (student_id, question, answer, answered_by, answered_at) VALUES
(2, 'What are the prerequisites for the Advanced Algorithms course?', 'You should have completed Data Structures and have a good understanding of basic algorithms. Mathematical maturity is also important.', 1, '2024-01-15 14:30:00'),
(4, 'How can I improve my coding skills for technical interviews?', 'Practice regularly on platforms like LeetCode and HackerRank. Focus on understanding problem patterns and time complexity analysis.', 3, '2024-01-16 10:15:00'),
(5, 'Is there any study group for the Database Systems course?', NULL, NULL, NULL),
(2, 'What programming languages should I focus on as a beginner?', 'Start with Python for its simplicity, then move to Java or C++ for understanding core concepts. JavaScript is great for web development.', 1, '2024-01-17 16:45:00');

-- Sample profiles
INSERT INTO profiles (user_id, class, section, bio) VALUES
(2, 'Computer Science', 'A', 'Passionate about software development and artificial intelligence. Love solving complex problems and learning new technologies.'),
(4, 'Computer Science', 'B', 'Interested in web development and mobile app creation. Currently learning React and Flutter.'),
(5, 'Information Technology', 'A', 'Fascinated by cybersecurity and network systems. Aspiring to become an ethical hacker.');

-- Create indexes for better performance
CREATE INDEX idx_posts_user_id ON posts(user_id);
CREATE INDEX idx_posts_created_at ON posts(created_at DESC);
CREATE INDEX idx_events_date ON events(event_date);
CREATE INDEX idx_events_created_by ON events(created_by);
CREATE INDEX idx_queries_student_id ON queries(student_id);
CREATE INDEX idx_queries_answered_by ON queries(answered_by);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- Display success message
SELECT 'Campus Connect database setup completed successfully!' as Status;

-- Show sample data counts
SELECT 
    (SELECT COUNT(*) FROM users) as Total_Users,
    (SELECT COUNT(*) FROM users WHERE role = 'teacher') as Teachers,
    (SELECT COUNT(*) FROM users WHERE role = 'student') as Students,
    (SELECT COUNT(*) FROM posts) as Total_Posts,
    (SELECT COUNT(*) FROM events) as Total_Events,
    (SELECT COUNT(*) FROM queries) as Total_Queries;
