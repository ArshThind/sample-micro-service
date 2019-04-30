package com.tutorial.service.order.dao;

import com.tutorial.commons.model.Product;
import com.tutorial.service.order.dao.entity.OrderEntity;

import java.util.List;

/**
 * An interface that defines the actions to be performed on the underlying @{@link javax.sql.DataSource}
 * relating to an @{@link com.tutorial.commons.model.Order}
 */
public interface OrdersDao {

    /**
     * Retrieves all the orders stored in the database.
     *
     * @return ArrayList of @{@link OrderEntity}
     */
    List<OrderEntity> getAllOrders();

    /**
     * Retrieves all the orders belonging to a particular user stored in the database.
     *
     * @param userId A valid user id
     * @return ArrayList of @{@link OrderEntity}
     */
    List<OrderEntity> getOrdersByUser(String userId);

    /**
     * Retrieves all the orders containing a particular product.
     *
     * @param productId A valid product id
     * @return ArrayList of @{@link OrderEntity}
     */
    List<OrderEntity> getOrdersByProduct(String productId);

    /**
     * Retrieves an order by an order id
     *
     * @param orderId Id of the order
     * @return An @{@link OrderEntity} matching the given order id
     */
    OrderEntity getOrdersById(String orderId);

    /**
     * Adds a new order to the database
     *
     * @param order new order to be added to the database.
     * @return true/false depending on whether the order addition was successful or not
     */
    boolean addOrder(OrderEntity order);

    /**
     * Adds a new @{@link Product} to an order.
     *
     * @param productId id of the product.
     * @param quantity  quantity of the product to be added.
     * @param orderId   id of the order.
     * @param userId    if of the user.
     * @return true/false depending on whether the product addition was successful or not.
     */
    boolean addProduct(int productId, int quantity, int orderId, int userId);

    /**
     * Cancels an existing order.
     *
     * @param orderId Id of the order to be cancelled
     * @return true if the order is cancelled. False if order doesn't exist or update fails.
     */
    boolean cancelOrder(String orderId);
}
