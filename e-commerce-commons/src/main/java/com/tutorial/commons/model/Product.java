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
     * product Description
     */
    private String description;

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

    public boolean isAvailable() {
        return this.availableQuantity > 0;
    }
}
