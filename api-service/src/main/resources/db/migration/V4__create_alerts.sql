CREATE TABLE alerts (
                        id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        product_id  UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
                        price_found NUMERIC(10,2) NOT NULL,
                        shipping    NUMERIC(10,2) NOT NULL DEFAULT 0,
                        total_price NUMERIC(10,2) NOT NULL,  -- o que o usuário realmente pagaria
                        source_url  VARCHAR(500),
                        marketplace VARCHAR(50) NOT NULL DEFAULT 'MERCADO_LIVRE',
                        seen        BOOLEAN NOT NULL DEFAULT false,
                        created_at  TIMESTAMP NOT NULL DEFAULT now()
);