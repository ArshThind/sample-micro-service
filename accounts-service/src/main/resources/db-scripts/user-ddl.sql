create table users (
id integer primary key ,
user_name varchar2 unique not null ,
email varchar2 unique not null ,
phone integer unique not null ,
permanent_address varchar2 not null ,
delivery_address varchar2 not null ,
saved_addresses varchar2
);