
-- Completar la migración de users eliminando la columna id antigua

ALTER TABLE users DROP COLUMN id;
ALTER TABLE users RENAME COLUMN id_uuid TO id;