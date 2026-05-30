CREATE TABLE users (
                       id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name       VARCHAR(100) NOT NULL,
                       email      VARCHAR(150) UNIQUE NOT NULL,
                       password   VARCHAR(255) NOT NULL,
                       cep        VARCHAR(9),   -- usado para calcular frete personalizado
                       created_at TIMESTAMP NOT NULL DEFAULT now()
);