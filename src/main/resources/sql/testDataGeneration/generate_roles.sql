INSERT INTO university.roles(name) 
VALUES 
('ROLE_ADMIN'),
('ROLE_STAFF'),
('ROLE_TEACHER'),
('ROLE_STUDENT');

INSERT INTO university.users_roles(user_id, role_id) 
VALUES 
(1, 1),
(2, 2);