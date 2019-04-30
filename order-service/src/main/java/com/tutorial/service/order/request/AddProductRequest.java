package com.tutorial.service.order.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple POJO encapsulating the request params for a request to add a new product to an existing order.
 */
@Getter
@Setter
public class AddProductRequest {

    /**
     * Id of the user
     */
    private int userId;

    /**
     * Id of the order
     */
    private int orderId;

    /**
     * Id of the product
     */
    private int productId;

    /**
     * Product quantity to be added
     */
    private int productQty;
}
