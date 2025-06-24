-- 🔧 Migración para convertir Foreign Keys de BIGINT a UUID
-- Archivo: V2__migrate_foreign_keys_to_uuid.sql

-- 🚨 IMPORTANTE: Esta migración asume que tienes datos existentes
-- Si no tienes datos, puedes simplificar eliminando las partes de UPDATE

-- ============================================
-- 1️⃣ MIGRAR matches.player1_id y player2_id
-- ============================================

-- Agregar columnas UUID temporales
ALTER TABLE matches ADD COLUMN player1_uuid UUID;
ALTER TABLE matches ADD COLUMN player2_uuid UUID;

-- Actualizar con UUIDs correspondientes (si tienes datos)
-- Nota: Esto solo funciona si tus datos actuales son consistentes
UPDATE matches
SET player1_uuid = (SELECT id_uuid FROM users WHERE users.id = matches.player1_id)
WHERE player1_id IS NOT NULL;

UPDATE matches
SET player2_uuid = (SELECT id_uuid FROM users WHERE users.id = matches.player2_id)
WHERE player2_id IS NOT NULL;

-- Eliminar columnas antiguas
ALTER TABLE matches DROP COLUMN player1_id;
ALTER TABLE matches DROP COLUMN player2_id;

-- Renombrar columnas UUID
ALTER TABLE matches RENAME COLUMN player1_uuid TO player1_id;
ALTER TABLE matches RENAME COLUMN player2_uuid TO player2_id;

-- ============================================
-- 2️⃣ MIGRAR tournament_players.user_id
-- ============================================

-- Agregar columna UUID temporal
ALTER TABLE tournament_players ADD COLUMN user_uuid UUID;

-- Actualizar con UUIDs correspondientes (si tienes datos)
UPDATE tournament_players
SET user_uuid = (SELECT id_uuid FROM users WHERE users.id = tournament_players.user_id)
WHERE user_id IS NOT NULL;

-- Eliminar columna antigua
ALTER TABLE tournament_players DROP COLUMN user_id;

-- Renombrar columna UUID
ALTER TABLE tournament_players RENAME COLUMN user_uuid TO user_id;

-- ============================================
-- 3️⃣ LIMPIAR tabla users (eliminar columna antigua)
-- ============================================

-- Ahora que todas las referencias están migradas, podemos limpiar users
-- Nota: Esto debería haberse hecho en la migración anterior, pero por seguridad lo hacemos aquí
-- ALTER TABLE users DROP COLUMN IF EXISTS id; -- Descomenta si la columna antigua aún existe