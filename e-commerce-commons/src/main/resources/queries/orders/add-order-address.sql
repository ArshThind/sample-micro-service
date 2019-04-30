INSERT INTO [DEL_ADDRESS]
(
order_id,
line_one,
city,
state,
pin_code
)
VALUES
(
LAST_INSERT_ID(),
:lineOne,
:city,
:state,
:pinCode
);