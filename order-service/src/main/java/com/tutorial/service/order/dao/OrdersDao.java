package com.tutorial.service.order.dao;

import com.tutorial.commons.model.Order;
import com.tutorial.commons.model.Product;

import java.util.List;

/**
 * An interface that defines the actions to be performed on the underlying @{@link javax.sql.DataSource}
 * relating to an @{@link com.tutorial.commons.model.Order}
 */
public interface OrdersDao {

    /**
     * Retrieves all the orders stored in the database.
     *
     * @return ArrayList of @{@link Order}
     */
    List<Order> getAllOrders();

    /**
     * Retrieves all the orders belonging to a particular user stored in the database.
     *
     * @param userId A valid user id
     * @return ArrayList of @{@link Order}
     */
    List<Order> getOrdersByUser(String userId);

    /**
     * Retrieves all the orders containing a particular product.
     *
     * @param productId A valid product id
     * @return ArrayList of @{@link Order}
     */
    List<Order> getOrdersByProduct(String productId);

    /**
     * Retrieves an order by an order id
     *
     * @param orderId Id of the order
     * @return An @{@link Order} matching the given order id
     */
    Order getOrdersById(String orderId);

    /**
     * Adds a new order to the database
     *
     * @param order new order to be added to the database.
     * @return true/false depening on whether the order addition was successful or not
     */
    boolean addOrder(Order order);

    /**
     * Updates an existing order in the database.
     *
     * @param order Order to be updated
     * @return true/false depending on whether the update was successful or not.
     */
    boolean updateOrder(Order order);

    /**
     * Deletes an existing order from the database.
     *
     * @param order Order to be deleted.
     * @return true/false depending on whether the removal was successful or not.
     */
    boolean deleteOrder(Order order);

    /**
     * Adds a new @{@link Product} to an order.
     *
     * @param productId id of the product.
     * @return true/false depending on whether the product addition was successful or not.
     */
    boolean addProduct(String productId);
}
