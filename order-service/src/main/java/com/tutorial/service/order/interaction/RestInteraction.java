package com.tutorial.service.order.interaction;

import com.tutorial.commons.model.Product;
import com.tutorial.commons.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Service which encapsulates the interaction mechanism to retrieve @{@link Product} details
 * and @{@link User} details via REST api calls.
 */
@Component
public class RestInteraction {

    /**
     * Calls the Products Service and retrieves the product details for the given product ids.
     *
     * @param productIds Product ids to be passed to products service.
     * @return HashMap from product id to the corresponding Product details.
     */
    public Map<Integer, Product> getProductDetails(Set<Integer> productIds) {
        return null;
    }

    /**
     * Calls the Accounts Service and retrieves the User details for the given user ids.
     *
     * @param userIds User ids to be passed to accounts service.
     * @return HashMap from user id to the corresponding User details.
     */
    public Map<Integer, User> getUserDetails(Set<Integer> userIds) {
        return null;
    }
}
