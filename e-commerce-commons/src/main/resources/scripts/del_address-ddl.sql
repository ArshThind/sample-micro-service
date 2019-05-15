CREATE TABLE `del_address` (
   `order_id` int(11) NOT NULL,
   `line_one` varchar(100) NOT NULL,
   `city` varchar(60) NOT NULL,
   `state` varchar(60) NOT NULL,
   `country` varchar(60) NOT NULL DEFAULT 'India',
   `pin_code` int(6) NOT NULL,
   PRIMARY KEY (`order_id`),
   CONSTRAINT `orders_fk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
 )