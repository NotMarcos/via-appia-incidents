-- 1) Tabela de usuários
CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP DEFAULT now()
);

-- 2) Tabela de incidents
CREATE TABLE incidents (
                           id SERIAL PRIMARY KEY,
                           title VARCHAR(100) NOT NULL,
                           description TEXT,
                           status VARCHAR(20) NOT NULL,
                           created_at TIMESTAMP DEFAULT now(),
                           updated_at TIMESTAMP DEFAULT now(),
                           created_by INT REFERENCES app_user(id)
);

-- 3) Tabela de comments
CREATE TABLE comments (
                          id SERIAL PRIMARY KEY,
                          incident_id INT REFERENCES incidents(id) ON DELETE CASCADE,
                          content TEXT NOT NULL,
                          created_at TIMESTAMP DEFAULT now()
);

-- 4) Usuários seed com senha bcrypt válida
-- Senha: admin123
INSERT INTO app_user (username, password, role) VALUES
    ('admin', '$2a$10$LOaNAsZLBtW4ktAWX09yUuaIpRaAb.lR/q8zjc35gaJu5T7cU496O', 'WRITE');

-- Senha: admin123
INSERT INTO app_user (username, password, role) VALUES
    ('viewer', '$2a$10$LOaNAsZLBtW4ktAWX09yUuaIpRaAb.lR/q8zjc35gaJu5T7cU496O', 'READ');