CREATE SCHEMA IF NOT EXISTS university;

CREATE TABLE IF NOT EXISTS university.users (
	id SERIAL PRIMARY KEY,
	username VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS university.roles (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS university.users_roles (
	user_id INTEGER,
	role_id INTEGER,
	PRIMARY KEY (user_id, role_id),
	FOREIGN KEY(user_id) REFERENCES university.users(id),
	FOREIGN KEY(role_id) REFERENCES university.roles(id)
);

CREATE TABLE IF NOT EXISTS university.groups (
	id SERIAL PRIMARY KEY, 
	name VARCHAR(50) NOT NULL, 
	num_students INTEGER
);

CREATE TABLE IF NOT EXISTS university.courses (
	id SERIAL PRIMARY KEY,
	name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS university.students (
	id SERIAL PRIMARY KEY, 
	first_name VARCHAR(50) NOT NULL, 
	last_name VARCHAR(50) NOT NULL, 
	group_id INTEGER, 
	user_id INTEGER,
	FOREIGN KEY (user_id) REFERENCES university.users (id), 
	FOREIGN KEY (group_id) REFERENCES university.groups(id) 
);

CREATE TABLE IF NOT EXISTS university.teachers (
	id SERIAL PRIMARY KEY, 
	first_name VARCHAR(50) NOT NULL, 
	last_name VARCHAR(50) NOT NULL,
	user_id INTEGER,
	FOREIGN KEY (user_id) REFERENCES university.users (id) 
);

CREATE TABLE IF NOT EXISTS university.students_courses (
	student_id INTEGER, 
	course_id INTEGER, 
	FOREIGN KEY (student_id) REFERENCES university.students(id),
	FOREIGN KEY (course_id) REFERENCES university.courses(id)
);

CREATE TABLE IF NOT EXISTS university.teachers_courses (
	teacher_id INTEGER, 
	course_id INTEGER, 
	FOREIGN KEY (teacher_id) REFERENCES university.teachers(id), 
	FOREIGN KEY (course_id) REFERENCES university.courses(id) 
);

CREATE TABLE IF NOT EXISTS university.schedule (
	id SERIAL PRIMARY KEY, 
	date_time TIMESTAMP NOT NULL, 
	group_id INTEGER, 
	course_id INTEGER, 
	FOREIGN KEY (group_id) REFERENCES university.groups(id), 
	FOREIGN KEY (course_id) REFERENCES university.courses(id) 
);