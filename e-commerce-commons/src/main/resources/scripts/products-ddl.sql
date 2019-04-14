create table if not exists products(
product_id int primary key auto_increment,
product_name varchar(100) not null,
category varchar(50) not null,
description varchar(150) not null,
unit_price double not null,
avail_qty int not null);