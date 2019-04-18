INSERT INTO [ORDERS]
(
user_id,
product_id,
product_qty
)
VALUES
(
:userId,
:productId,
:productQty
);

-- INSERT INTO [ORDER_STATUS]
-- (
-- order_id,
-- status
-- )
-- VALUES
-- (
-- LAST_INSERT_ID(),
-- 'A'
-- );
--
-- INSERT INTO [DEL_ADDRESS]
-- (
-- order_id,
-- line_one,
-- city,
-- state
-- )
-- VALUES
-- (
-- LAST_INSERT_ID,
-- :lineOne,
-- :city,
-- :state
-- );