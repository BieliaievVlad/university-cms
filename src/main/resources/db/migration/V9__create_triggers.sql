CREATE TRIGGER student_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON university.students
FOR EACH ROW
EXECUTE FUNCTION log_audit();

CREATE TRIGGER course_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON university.courses
FOR EACH ROW
EXECUTE FUNCTION log_audit();

CREATE TRIGGER teacher_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON university.teachers
FOR EACH ROW
EXECUTE FUNCTION log_audit();

CREATE TRIGGER group_audit_trigger 
AFTER INSERT OR UPDATE OR DELETE ON university.groups 
FOR EACH ROW 
EXECUTE FUNCTION log_audit();

CREATE TRIGGER group_course_audit_trigger 
AFTER INSERT OR UPDATE OR DELETE ON university.groups_courses 
FOR EACH ROW 
EXECUTE FUNCTION log_audit();

CREATE TRIGGER teacher_course_audit_trigger 
AFTER INSERT OR UPDATE OR DELETE ON university.teachers_courses 
FOR EACH ROW 
EXECUTE FUNCTION log_audit();

CREATE TRIGGER user_audit_trigger 
AFTER INSERT OR UPDATE OR DELETE ON university.users 
FOR EACH ROW 
EXECUTE FUNCTION log_audit();