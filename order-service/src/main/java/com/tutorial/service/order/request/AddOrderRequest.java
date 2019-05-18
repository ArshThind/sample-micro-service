package com.tutorial.service.order.request;

import com.tutorial.commons.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Simple POJO encapsulating the request params for request to add a new order.
 */
@Getter
@Setter
public class AddOrderRequest {

    /**
     * Id of the user
     */
    private int userId;

    /**
     * Delivery address for the product
     */
    private Address address;

    /**
     * HashMap which maps productIds to their corresponding quantities
     */
    private Map<String, Integer> productQty;

}
