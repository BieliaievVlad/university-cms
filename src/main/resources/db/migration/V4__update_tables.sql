DROP TABLE IF EXISTS university.students_courses CASCADE;

CREATE TABLE IF NOT EXISTS university.groups_courses (
	group_id INTEGER,
	course_id INTEGER,
	FOREIGN KEY (group_id) REFERENCES university.groups(id),
	FOREIGN KEY (course_id) REFERENCES university.courses(id)
);

SELECT setval('university.courses_id_seq', (SELECT MAX(id) FROM university.courses));

ALTER TABLE university.teachers_courses
DROP CONSTRAINT IF EXISTS teachers_courses_pkey;

ALTER TABLE university.teachers_courses
ADD CONSTRAINT teachers_courses_pkey PRIMARY KEY (teacher_id, course_id);

INSERT INTO university.teachers (id, first_name, last_name) 
VALUES 
(0, 'dummy', 'dummy');