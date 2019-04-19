create table del_address(order_id int references orders,
line_one varchar(100) not null,
city varchar(60) not null,
state varchar(60) not null,
pin_code int(6) not null,
country varchar(60)  not null default 'India',
primary key (order_id));