UPDATE university.groups g 
SET num_students = (
    SELECT COUNT(*)
    FROM university.students s
    WHERE s.group_id = g.id
);