SELECT o2.*,a.* FROM [ORDERS] o1
JOIN [DEL_ADDRESS] a
ON o1.order_id = a.order_id
JOIN [ORDERS] o2
ON o1.order_id = o2.order_id
WHERE o1.product_id = :productId
AND o2.order_id = o1.order_id