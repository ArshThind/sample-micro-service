package com.tutorial.service.order.controller;

import com.tutorial.commons.exceptions.BadInputException;
import com.tutorial.commons.model.Order;
import com.tutorial.service.order.request.AddOrderRequest;
import com.tutorial.service.order.request.AddProductRequest;
import com.tutorial.service.order.service.OrdersService;
import com.tutorial.service.order.util.OrdersValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * A Spring REST controller that provides api endpoints to perform operations on @{@link com.tutorial.commons.model.Order}
 */
@RestController
@RequestMapping("/orders")
@Slf4j
public class OrdersController {

    private OrdersService ordersService;

    private OrdersValidator validator;

    /**
     * Parameterized constructor that is used to wire in the @{@link OrdersService} and @{@link OrdersValidator}
     *
     * @param ordersService orderService implementation
     * @param validator     inputValidator implementation
     */
    @Autowired
    public OrdersController(OrdersService ordersService, OrdersValidator validator) {
        Assert.notNull(ordersService, "Order Service should not be null");
        Assert.notNull(validator, "Orders Validator should not be null");
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
        try {
            return ordersService.getAllOrders();
        } catch (Exception e) {
            log.error("Error occurred while fetching the orders: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REST end point to query the orders based on product id.
     *
     * @param productId Id of the product
     * @return Array List of orders containing the product
     */
    @GetMapping(path = "/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> getOrdersByProduct(@PathVariable("productId") String productId) {
        try {
            validator.validateProductId(productId);
            List<Order> orders = ordersService.getOrdersByProduct(productId);
            checkEmpty(orders);
            return orders;
        } catch (BadInputException e) {
            throw e;
        } catch (WebApplicationException e) {
            log.error("Error occurred while fetching the orders: {}", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while getting orders, Error: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REST end point to query the orders based on user id.
     *
     * @param userId id of the user
     * @return Array List of orders belonging to the user.
     */
    @GetMapping(path = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> getOrdersByUser(@PathVariable("userId") String userId) {
        try {
            validator.validateUserId(userId);
            List<Order> orders = ordersService.getOrdersByUser(userId);
            checkEmpty(orders);
            return orders;
        } catch (BadInputException e) {
            throw e;
        } catch (WebApplicationException e) {
            log.error("Error occurred while fetching the orders: {}", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while getting orders, Error: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REST end point to query order based on product id.
     *
     * @param orderId id of the order
     * @return Order having the same id as the passed input id, if exists else returns Http Status 204.
     */
    @GetMapping(path = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order getOrderById(@PathVariable("orderId") String orderId) {
        try {
            validator.validateOrderId(orderId);
            Order order = ordersService.getOrderById(orderId);
            if (order == null) {
                throw new WebApplicationException(Response.Status.NO_CONTENT);
            }
            return order;
        } catch (BadInputException e) {
            throw e;
        } catch (WebApplicationException e) {
            log.error("Error occurred while fetching the orders: {}", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while getting orders, Error: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    // ~ PUT & POST Methods
    // ========================================================================================================

    /**
     * REST endpoint to add product to an existing order.
     *
     * @param request object encapsulating the request params.
     * @return Http Status 200 if successful else returns Http Status 503
     */
    @PutMapping(path = "/product", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response addProduct(@RequestBody AddProductRequest request) {
        try {
            if (ordersService.addProduct(request)) {
                return Response.status(Response.Status.OK).build();
            }
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(request).build();
        } catch (WebApplicationException e) {
            log.error("Error occurred while fetching the orders: {}", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while adding product, Error: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REST endpoint to create a new order.
     *
     * @param order order to be created.
     * @return Http Status 200 if successful else returns Http Status 503
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response createNewOrder(@RequestBody AddOrderRequest order) {
        try {
            validator.validateCreateOrderRequest(order);
            if (ordersService.createOrder(order)) {
                return Response.ok().build();
            }
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        } catch (BadInputException e) {
            throw e;
        } catch (WebApplicationException e) {
            log.error("Error occurred while fetching the orders: {}", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while creating a new order, Error: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REST end point to cancel an existing order.
     *
     * @param orderId id of the order to be cancelled
     * @return Http Status 200 if successful else returns Http Status 503
     */
    @DeleteMapping(path = "/{orderId}")
    public Response cancelOrder(@PathVariable("orderId") String orderId) {
        try {
            validator.validateOrderId(orderId);
            if (ordersService.cancelOrder(orderId)) {
                return Response.accepted().build();
            }
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        } catch (BadInputException e) {
            throw e;
        } catch (WebApplicationException e) {
            log.error("Error occurred while fetching the orders: {}", e);
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while cancelling the order, Error: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Utility method to check if the list is empty.
     */
    private void checkEmpty(List<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        }
    }
}
