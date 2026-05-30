-- Economia calculada sobre total_price (preço + frete)
-- Apenas quando total_price < reference_price — sem números negativos
CREATE VIEW user_savings AS
SELECT
    p.user_id,
    DATE_TRUNC('month', a.created_at)            AS month,
  COUNT(*)                                      AS alerts_count,
  SUM(p.reference_price - a.total_price)        AS total_saved,
  MAX(p.reference_price - a.total_price)        AS best_deal,
  MIN(a.total_price)                            AS lowest_total_found,
  MIN(a.shipping)                               AS lowest_shipping_found
FROM alerts a
JOIN products p ON p.id = a.product_id
WHERE a.total_price < p.reference_price
GROUP BY p.user_id, DATE_TRUNC('month', a.created_at);