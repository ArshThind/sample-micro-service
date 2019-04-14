package com.tutorial.commons.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple POJO class representing a product
 */
@Getter
@Setter
public class Product {

    /**
     * Util method to create a dummy product. Useful for mocking while developing.
     *
     * @return dummy product
     */
    public static final Product createDummyProduct() {
        Product product = new Product();
        product.productId = 999;
        product.isAvailable = true;
        product.category = "test";
        product.availableQuantity = 999;
        product.name = "test-product";
        product.price = 999;
        return product;
    }

    /**
     * product ID
     */
    private long productId;

    /**
     * product Name
     */
    private String name;

    /**
     * product price
     */
    private String category;

    /**
     * product Price
     */
    private double price;

    /**
     * product available quantity
     */
    private int availableQuantity;

    /**
     * product availability flag
     */
    private boolean isAvailable;

}
