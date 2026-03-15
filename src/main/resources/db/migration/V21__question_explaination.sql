ALTER TABLE questions_v2
ALTER COLUMN explanation TYPE varchar(1000) USING explanation::varchar(1000);