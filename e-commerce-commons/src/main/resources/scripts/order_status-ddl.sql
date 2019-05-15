CREATE TABLE `order_status` (
   `order_id` int(11) NOT NULL,
   `status` char(1) DEFAULT NULL,
   PRIMARY KEY (`order_id`),
   CONSTRAINT `fk_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
 );