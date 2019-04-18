package com.tutorial.service.order.interaction;

import com.tutorial.commons.model.Product;
import com.tutorial.commons.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class RestInteraction {

    public Map<Integer, Product> getProductDetails(Set<Integer> productIds) {
        return null;
    }

    public Map<Integer, User> getUserDetails(Set<Integer> userIds) {
        return null;
    }
}
