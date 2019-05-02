package com.tutorial.service.order.interaction.feign;

import com.tutorial.commons.model.Product;
import com.tutorial.commons.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * Declarative rest client which utilizes feign to make REST calls to other micro-services registered in
 * the service-registry.
 */
@FeignClient(value = "orchestrator", path = FeignRestClient.ORCHESTRATOR_PATH)
public interface FeignRestClient {

    String ORCHESTRATOR_PATH = "e-commerce/rest/api";

    String PRODUCTS_PATH = "products/products";

    String USERS_PATH = "accounts/users";

    /**
     * Calls the products-service via the service gateway and wraps the result in an {@link Optional}
     *
     * @param productIds comma separated product ids
     * @return ArrayList of products wrapped in an optional
     */
    @RequestMapping(path = PRODUCTS_PATH, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    Optional<List<Product>> getProducts(@RequestParam(value = "productIds") String productIds);

    /**
     * Calls the accounts-service via the service gateway and wraps the result in an {@link Optional}
     *
     * @param userIds comma separated user ids
     * @return Array List of users wrapped in an optional
     */
    @RequestMapping(path = USERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    Optional<List<User>> getUsers(@RequestParam(value = "userIds") String userIds);

}
