package com.tutorial.service.order.controller;

import com.tutorial.commons.model.Order;
import com.tutorial.commons.utils.InputEntityValidator;
import com.tutorial.service.order.service.OrdersService;
import com.tutorial.service.order.util.OrdersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * A Spring REST controller that provides api endpoints to perform operations on @{@link com.tutorial.commons.model.Order}
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    private OrdersService ordersService;

    private InputEntityValidator validator;

    /**
     * Parameterized constructor that is used to wire in the @{@link OrdersService} and @{@link OrdersValidator}
     *
     * @param ordersService orderService implementation
     * @param validator     inputValidator implementation
     */
    @Autowired
    public OrdersController(OrdersService ordersService, InputEntityValidator validator) {
        Assert.notNull(ordersService, "Order Service should not be null");
        Assert.notNull(validator, "Orders InputEntityValidator should not be null");
        this.ordersService = ordersService;
        this.validator = validator;
    }

    // ~ GET METHODS
    // ========================================================================================================

    /**
     * A REST end point to retrieve all the orders registered with the service.
     *
     * @return ArrayList of all the orders
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> getAllOrders() {
        return Arrays.asList(Order.createDummyOrder());
    }

    /**
     * REST end pount to query the orders based on product id.
     *
     * @param productId Id of the product
     * @return Array List of orders containing the product
     */
    @GetMapping(path = "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> getOrdersByProduct(@PathVariable("productId") String productId) {
        return null;
    }

    /**
     * REST end point to query the orders based on user id.
     *
     * @param userId id of the user
     * @return Array List of orders belonging to the user.
     */
    @GetMapping(path = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> getOrdersByUser(@PathVariable("userId") String userId) {
        return null;
    }

    /**
     * REST end point to query order based on product id.
     *
     * @param orderId id of the order
     * @return Order having the same id as the passed input id.
     */
    @GetMapping(path = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order getOrderById(@PathVariable("orderId") String orderId) {
        return null;
    }


    // ~ PUT & POST Methods
    // ========================================================================================================

    /**
     * REST endpoint to add product to an existing order.
     *
     * @param productId id of the product to be added.
     * @return true/false depending on whether the product addition was successful or not.
     */
    @PutMapping(path = "products/{productId}")
    public boolean addProduct(@PathVariable("productId") String productId) {
        return true;
    }

    /**
     * REST endpoint to create a new order.
     *
     * @param order order to be created.
     * @return true/false depending on whether the order creation was successful or not.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean createNewOrder(Order order) {
        return true;
    }

    /**
     * REST end point to cancel an existing order.
     *
     * @param orderId id of the order to be cancelled
     * @return true/false depending on whether the order was successfully cancelled or not.
     */
    @PostMapping(path = "/{orderId}")
    public boolean cancelOrder(@PathVariable("orderId") String orderId) {
        return true;
    }
}
