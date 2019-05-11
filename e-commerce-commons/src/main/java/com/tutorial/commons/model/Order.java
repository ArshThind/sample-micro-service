package com.tutorial.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * Simple POJO representing a Order.
 */

@Getter
@Setter
@ToString
public class Order {

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
