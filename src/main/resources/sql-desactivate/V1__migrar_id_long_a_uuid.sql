-- 🧠 Habilita la extensión necesaria para usar gen_random_uuid()
-- Asegúrate de tener privilegios para esto (una sola vez por base)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 👣 Paso 1: Agrega una nueva columna UUID temporal
ALTER TABLE users ADD COLUMN id_uuid UUID;

-- 👣 Paso 2: Rellena la columna con UUIDs generados automáticamente
UPDATE users SET id_uuid = gen_random_uuid();

-- 👣 Paso 3: Elimina claves foráneas que apunten a users.id, si existieran (aquí no hay)

-- 👣 Paso 4: Elimina constraint PRIMARY KEY actual
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_pkey;

-- 👣 Paso 5: Borra la columna antigua id tipo BIGINT
ALTER TABLE users DROP COLUMN id;

-- 👣 Paso 6: Renombra la columna temporal UUID como id
ALTER TABLE users RENAME COLUMN id_uuid TO id;

-- 👣 Paso 7: Declara el nuevo campo como clave primaria
ALTER TABLE users ADD PRIMARY KEY (id);