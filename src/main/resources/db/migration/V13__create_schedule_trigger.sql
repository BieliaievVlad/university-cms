CREATE TRIGGER schedule_audit_trigger 
AFTER INSERT OR UPDATE OR DELETE ON university.schedule 
FOR EACH ROW 
EXECUTE FUNCTION log_audit();