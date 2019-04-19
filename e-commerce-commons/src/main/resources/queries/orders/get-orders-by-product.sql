SELECT * FROM [ORDERS] o
JOIN [DEL_ADDRESS] a
ON o.order_id = a.order_id
WHERE product_id = :productId