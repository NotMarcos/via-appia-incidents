CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE incident (
                          id UUID PRIMARY KEY,
                          titulo VARCHAR(120) NOT NULL,
                          descricao TEXT,
                          prioridade VARCHAR(20) NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          responsavel_email VARCHAR(255) NOT NULL,
                          data_abertura TIMESTAMP NOT NULL,
                          data_atualizacao TIMESTAMP NOT NULL
);

CREATE TABLE incident_tags (
                               incident_id UUID REFERENCES incident(id) ON DELETE CASCADE,
                               tag VARCHAR(255) NOT NULL
);

CREATE TABLE comment (
                         id UUID PRIMARY KEY,
                         incident_id UUID REFERENCES incident(id) ON DELETE CASCADE,
                         autor VARCHAR(255) NOT NULL,
                         mensagem TEXT NOT NULL,
                         data_criacao TIMESTAMP NOT NULL
);