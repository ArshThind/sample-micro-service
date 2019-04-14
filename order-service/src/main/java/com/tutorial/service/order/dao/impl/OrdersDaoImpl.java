package com.tutorial.service.order.dao.impl;

import com.tutorial.commons.model.Order;
import com.tutorial.commons.utils.QueryProvider;
import com.tutorial.service.order.dao.OrdersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Base implementation for @{@link OrdersDao} which performs
 */
@Repository
public class OrdersDaoImpl implements OrdersDao {

    private QueryProvider queryProvider;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public OrdersDaoImpl(QueryProvider queryProvider, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.queryProvider = queryProvider;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId
     * @return
     */
    @Override
    public List<Order> getOrdersByUser(String userId) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param productId
     * @return
     */
    @Override
    public List<Order> getOrdersByProduct(String productId) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Order getOrdersById(String orderId) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean addOrder(Order order) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean updateOrder(Order order) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean deleteOrder(Order order) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param productId id of the product.
     * @return
     */
    @Override
    public boolean addProduct(String productId) {
        return false;
    }
}
