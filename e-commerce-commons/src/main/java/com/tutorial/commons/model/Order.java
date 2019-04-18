package com.tutorial.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

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
        order.id = -1;
        order.user = User.createDummyUser();
        order.cost = 999d;
        order.address = Address.createDummyAddress();
        Map<Product, Integer> productQty = new HashMap<>(1);
        productQty.put(Product.createDummyProduct(), 1);
        return order;
    }

    /**
     * Id of the order
     */
    private Integer id;

    /**
     * The @{@link User} to which this order belongs to.
     */
    private User user;

    /**
     * HashMap mapping products to their quantities.
     */
    private Map<Product, Integer> products;

    /**
     * Delivery address of the order.
     */
    private Address address;

    /**
     * Total cost of the order.
     */
    private Double cost;
}
