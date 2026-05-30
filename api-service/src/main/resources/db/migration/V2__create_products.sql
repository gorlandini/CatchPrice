CREATE TABLE products (
                          id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          name            VARCHAR(200) NOT NULL,
                          ml_query        VARCHAR(200) NOT NULL,
                          reference_price NUMERIC(10,2),
                          active          BOOLEAN NOT NULL DEFAULT true,
                          created_at      TIMESTAMP NOT NULL DEFAULT now()
);