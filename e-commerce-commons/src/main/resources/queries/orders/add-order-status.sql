INSERT INTO [ORDER_STATUS]
(
order_id,
status
)
VALUES
(
LAST_INSERT_ID(),
'A'
);