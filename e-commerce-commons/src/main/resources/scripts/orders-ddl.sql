create table if not exists orders(
order_id int primary key auto_increment,
user_id int references users,
product_id int references products,
product_qty int not null,
constraint unq_order unique(order_id,user_id,product_id)
);