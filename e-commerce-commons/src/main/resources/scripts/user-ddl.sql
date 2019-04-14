create table if not exists users(user_id int primary key auto_increment,
user_name varchar(70) unique not null,
email varchar(100) unique not null,
phone_num int(10) unique not null,
user_type char(1) not null,
status char(1) not null,
valid_from datetime default current_timestamp);