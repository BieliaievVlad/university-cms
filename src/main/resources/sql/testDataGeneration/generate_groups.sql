INSERT INTO university.groups (name)
SELECT CONCAT(
    chr(floor(random() * 26 + 65)::integer),
    chr(floor(random() * 26 + 65)::integer),
    '-',
    lpad(floor(random() * 100)::text, 2, '0')
)
FROM generate_series(1, 10);