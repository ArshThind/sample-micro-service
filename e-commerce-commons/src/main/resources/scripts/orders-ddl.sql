CREATE TABLE `orders` (
   `order_id` int(11) NOT NULL AUTO_INCREMENT,
   `user_id` int(11) NOT NULL,
   `product_id` int(11) NOT NULL,
   `product_qty` int(11) NOT NULL,
   PRIMARY KEY (`order_id`,`user_id`,`product_id`),
   UNIQUE KEY `unq_order` (`order_id`,`user_id`),
   KEY `fk_user` (`user_id`),
   KEY `fk_product` (`product_id`),
   CONSTRAINT `fk_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
   CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
 )