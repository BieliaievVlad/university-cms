CREATE TABLE IF NOT EXISTS university.audit_log (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(255),
    table_name VARCHAR(255),
    operation_type VARCHAR(50),
    old_data TEXT,
    new_data TEXT
);