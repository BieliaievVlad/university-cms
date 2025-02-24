CREATE OR REPLACE FUNCTION log_audit() 
RETURNS TRIGGER AS 
$$ 
DECLARE 
    op_type VARCHAR(50);
    old_data TEXT;
    new_data TEXT;
    user_name VARCHAR(255);
BEGIN 

	SELECT name INTO user_name FROM university.current_user_session LIMIT 1;

    IF TG_OP = 'INSERT' THEN 
        op_type := 'CREATE';
        new_data := row_to_json(NEW)::TEXT;
        old_data := NULL;
    ELSIF TG_OP = 'UPDATE' THEN 
        op_type := 'UPDATE';
        new_data := row_to_json(NEW)::TEXT;
        old_data := row_to_json(OLD)::TEXT;
    ELSIF TG_OP = 'DELETE' THEN 
        op_type := 'DELETE';
        new_data := NULL;
        old_data := row_to_json(OLD)::TEXT;
    END IF; 

    INSERT INTO university.audit_log (username, table_name, operation_type, old_data, new_data)
    VALUES (user_name, TG_TABLE_NAME, op_type, old_data, new_data);

    RETURN NEW;
END;
$$
LANGUAGE plpgsql;