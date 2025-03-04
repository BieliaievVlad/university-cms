ALTER TABLE university.audit_log
ALTER COLUMN old_data TYPE JSON USING old_data::JSON,
ALTER COLUMN new_data TYPE JSON USING new_data::JSON;