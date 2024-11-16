INSERT INTO university.students_courses (student_id, course_id)
SELECT s.id, c.id 
FROM university.students s 
CROSS JOIN LATERAL (
    SELECT c.id, 
           ROW_NUMBER() OVER (PARTITION BY s.id ORDER BY RANDOM()) AS row_num
    FROM university.courses c
    WHERE RANDOM() < 0.5  
    LIMIT FLOOR(RANDOM() * 3) + 1  
) AS c;