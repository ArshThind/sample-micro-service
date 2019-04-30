package com.tutorial.service.order.service;

import com.tutorial.commons.model.Order;
import com.tutorial.service.order.request.AddOrderRequest;
import com.tutorial.service.order.request.AddProductRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Defines the contract for the to be implemented by the implementations
 * which provide services relating to @{@link com.tutorial.commons.model.Order}
 */
public interface OrdersService {

    /**
     * Retrieves all orders registered with the service.
     *
     * @return ArrayList of @{@link Order}
     */
    List<Order> getAllOrders();

    /**
     * Retrieves all the orders belonging to a particular user.
     *
     * @param userId A valid user id
     * @return ArrayList of @{@link Order}
     */
    List<Order> getOrdersByUser(String userId);

    /**
     * Retrieves all the orders which contains the particular product.
     *
     * @param productId A valid product id
     * @return ArrayList of @{@link Order}
     */
    List<Order> getOrdersByProduct(String productId);

    /**
     * Retrieves the order having with the particular id.
     *
     * @param orderId Id of the Order
     * @return Order corresponding to the id
     */
    Order getOrderById(String orderId);

    /**
     * Registers a new order with the service.
     *
     * @param order Order to be registered
     * @return true/false depending on whether the order was registered successfully or not.
     */
    @Transactional(rollbackFor = DataAccessException.class)
    boolean createOrder(AddOrderRequest order);

    /**
     * Cancels an order registered with the service.
     *
     * @param orderId Order to be cancelled.
     * @return true/false depending on whether the order was cancelled successfully or not.
     */
    boolean cancelOrder(String orderId);

    /**
     * Adds a new product to the order.
     *
     * @param request object encapsulating the request params
     * @return true/false depending on whether the product was added successfully or not.
     */
    boolean addProduct(AddProductRequest request);
}
