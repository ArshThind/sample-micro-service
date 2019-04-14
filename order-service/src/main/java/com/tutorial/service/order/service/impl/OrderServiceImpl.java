package com.tutorial.service.order.service.impl;

import com.tutorial.commons.model.Order;
import com.tutorial.service.order.dao.OrdersDao;
import com.tutorial.service.order.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrdersService {

    private OrdersDao ordersDao;

    @Autowired
    public OrderServiceImpl(OrdersDao ordersDao) {
        this.ordersDao = ordersDao;
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
     * @return
     * @param userId
     */
    @Override
    public List<Order> getOrdersByUser(String userId) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     * @param productId
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
    public Order getOrdersById(String id) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean createOrder(Order order) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean cancelOrder(Order order) {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     * @param productId
     */
    @Override
    public boolean addProduct(String productId) {
        return false;
    }
}
