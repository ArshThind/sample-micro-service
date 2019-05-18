package com.tutorial.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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
     * Products in the order
     */
    List<Product> products;

    /**
     * Delivery address of the order.
     */
    private Address address;

    /**
     * Total cost of the order.
     */
    private Double cost;
}
