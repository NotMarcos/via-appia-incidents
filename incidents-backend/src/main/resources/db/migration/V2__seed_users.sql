-- Usuários seed
-- Senha: admin123 (bcrypt)
INSERT INTO app_user (username, password, role) VALUES
    ('admin', '$2a$10$LOaNAsZLBtW4ktAWX09yUuaIpRaAb.lR/q8zjc35gaJu5T7cU496O', 'WRITE');

-- Senha: admin123 (bcrypt)
INSERT INTO app_user (username, password, role) VALUES
    ('viewer', '$2a$10$LOaNAsZLBtW4ktAWX09yUuaIpRaAb.lR/q8zjc35gaJu5T7cU496O', 'READ');
