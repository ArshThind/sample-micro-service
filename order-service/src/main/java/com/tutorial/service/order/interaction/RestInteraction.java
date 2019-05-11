package com.tutorial.service.order.interaction;

import com.tutorial.commons.model.Product;
import com.tutorial.commons.model.User;
import com.tutorial.service.order.interaction.feign.FeignRestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service which encapsulates the interaction mechanism to retrieve @{@link Product} details
 * and @{@link User} details via REST api calls.
 */
@Component
@Slf4j
public class RestInteraction {

    private FeignRestClient restClient;

    @Autowired
    public RestInteraction(FeignRestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Calls the Products Service and retrieves the product details for the given product ids.
     *
     * @param productIdSet Product ids to be passed to products service.
     * @return HashMap from product id to the corresponding Product details.
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, Product> getProductDetails(Set<Integer> productIdSet) {
        String productIds = StringUtils.join(productIdSet, ",");
        Optional<List<Product>> response = restClient.getProducts(productIds);
        if (response.isPresent()) {
            return response.get().stream()
                    .collect(Collectors.toMap(s -> (int) s.getProductId(), Function.identity()));
        }
        return Collections.EMPTY_MAP;
    }

    /**
     * Calls the Accounts Service and retrieves the User details for the given user ids.
     *
     * @param userIdSet User ids to be passed to accounts service.
     * @return HashMap from user id to the corresponding User details.
     */
    @SuppressWarnings("unchecked")
    public Map<Integer, User> getUserDetails(Set<Integer> userIdSet) {
        String userIds = StringUtils.join(userIdSet, ",");
        Optional<List<User>> response = restClient.getUsers(userIds);
        if (response.isPresent()) {
            return response.get().stream()
                    .collect(Collectors.toMap(s -> (int) s.getId(), Function.identity()));
        }
        return Collections.EMPTY_MAP;
    }
}
