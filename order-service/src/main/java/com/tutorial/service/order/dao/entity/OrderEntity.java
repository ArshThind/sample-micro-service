package com.tutorial.service.order.dao.entity;

import com.tutorial.commons.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Simple POJO which acts as an intermediary between the DB model and the data model for the view
 * for an {@link com.tutorial.commons.model.Order}.
 */
@Getter
@Setter
public class OrderEntity {

    /**
     * Id of the order
     */
    private int orderId;

    /**
     * Id of the user
     */
    private int userId;

    /**
     * HashMap which maps the product ids to their corresponding quantities for an order.
     */
    private Map<Integer, Integer> productQtyMap;

    /**
     * Delivery address for an order.
     */
    private Address address;
}
