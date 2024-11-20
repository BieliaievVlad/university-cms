INSERT INTO university.students (first_name, last_name) 
SELECT  
    n1.first_name, 
    n2.last_name 
FROM  
    university.randomNames n1, university.randomNames n2 
WHERE  
    n1.first_name <> n2.first_name  
ORDER BY  
    RANDOM() 
LIMIT 200;

UPDATE university.students 
SET group_id = ROUND(random() * 9 + 1)::INTEGER;