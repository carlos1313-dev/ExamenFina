INSERT INTO users (email, full_name, username, password, is_active, created_at, updated_at) 
VALUES 
('test@instagram.com', 'Usuario Test', 'testuser', 'password123', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('john@example.com', 'John Doe', 'johndoe', 'mypassword', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jane@example.com', 'Jane Smith', 'janesmith', 'secretpass', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
