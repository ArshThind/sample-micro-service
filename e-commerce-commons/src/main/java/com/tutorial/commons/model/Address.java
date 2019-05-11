package com.tutorial.commons.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple POJO representing the delivery address for an order.
 */
@Getter
@Setter
public class Address {

    private static final String COUNTRY = "India";

    /**
     * Main address line for the address.
     */
    private String addressLine;

    /**
     * A valid city.
     */
    private String city;

    /**
     * State for the city.
     */
    private String state;

    /**
     * A valid 6 digit PIN code for the address.
     */
    private int pinCode;
}
