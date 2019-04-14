package com.tutorial.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple POJO representing a Order.
 */

@Getter
@Setter
@ToString
public class Order {

    /**
     * Dummy method to create a mock Order. Useful during development.
     *
     * @return Dummy Order
     */
    public static final Order createDummyOrder() {
        Order order = new Order();
        order.id = "999999";
        order.user = User.createDummyUser();
        order.cost = 999d;
        Set<Product> products = new HashSet<>(1);
        products.add(Product.createDummyProduct());
        order.products = products;
        order.address = Address.createDummyAddress();
        return order;
    }

    /**
     * Id of the order
     */
    private String id;

    /**
     * The @{@link User} to which this order belongs to.
     */
    private User user;

    /**
     * Unique product(s) in this order.
     */
    private Set<Product> products;

    /**
     * Delivery address of the order.
     */
    private Address address;

    /**
     * Total cost of the order.
     */
    private Double cost;
}
