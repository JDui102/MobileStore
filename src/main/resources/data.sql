-- Init Roles
INSERT INTO roles (id, name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_MEMBER')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Init Users
INSERT INTO users (id, username, email, password, role) VALUES
(1, 'admin', 'admin@gmail.com', '$2a$10$cFrcpAIfkpHhw/nnILqMDeVmzt1ZGFUzydoxUiCNn7Q7Qn/Yuvbaa', 1)
ON DUPLICATE KEY UPDATE username=VALUES(username), email=VALUES(email), password=VALUES(password), role=VALUES(role);