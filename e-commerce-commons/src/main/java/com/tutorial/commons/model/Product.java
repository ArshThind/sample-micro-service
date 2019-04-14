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
    private boolean availabilityFlag;

    /**
     * @return Product availability flag
     */
    public boolean isAvailable() {
        return this.availabilityFlag;
    }

    /**
     * Set the product availability flag
     *
     * @param availabilityFlag boolean value
     */
    public void setAvailabilityFlag(boolean availabilityFlag) {
        this.availabilityFlag = availabilityFlag;
    }
}
