-- Agregar columna UUID temporal
ALTER TABLE messages ADD COLUMN sender_uuid UUID;

-- Actualizar con UUIDs correspondientes (si tienes datos)
UPDATE messages
SET sender_uuid = (SELECT id_uuid FROM users WHERE users.id = messages.sender_id)
WHERE sender_id IS NOT NULL;

-- Eliminar columna antigua
ALTER TABLE messages DROP COLUMN sender_id;

-- Renombrar columna UUID
ALTER TABLE messages RENAME COLUMN sender_uuid TO sender_id;
