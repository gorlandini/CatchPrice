CREATE TABLE price_history (
                               id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               product_id  UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
                               price       NUMERIC(10,2) NOT NULL,
                               shipping    NUMERIC(10,2) NOT NULL DEFAULT 0,  -- frete calculado por CEP
                               total_price NUMERIC(10,2) NOT NULL,            -- price + shipping
                               source_url  VARCHAR(500),
                               marketplace VARCHAR(50) NOT NULL DEFAULT 'MERCADO_LIVRE',  -- suporte futuro
                               checked_at  TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_price_history_product_date
    ON price_history(product_id, checked_at DESC);