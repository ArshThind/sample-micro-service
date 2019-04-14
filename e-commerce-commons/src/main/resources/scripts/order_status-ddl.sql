create table order_status(order_id int references orders,
status char(1),
primary key(order_id));