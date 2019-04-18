package com.tutorial.service.order.service.impl;

import com.tutorial.commons.model.Order;
import com.tutorial.commons.model.Product;
import com.tutorial.commons.model.User;
import com.tutorial.service.order.dao.OrdersDao;
import com.tutorial.service.order.dao.entity.OrderEntity;
import com.tutorial.service.order.interaction.RestInteraction;
import com.tutorial.service.order.request.OrderRequest;
import com.tutorial.service.order.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrdersService {

    private OrdersDao ordersDao;

    private RestInteraction interaction;

    @Autowired
    public OrderServiceImpl(OrdersDao ordersDao, RestInteraction interaction) {
        this.ordersDao = ordersDao;
        this.interaction = interaction;
    }

    @Override
    public List<Order> getAllOrders() {
        return constructOrder(ordersDao.getAllOrders());
    }

    @Override
    public List<Order> getOrdersByUser(String userId) {
        return constructOrder(ordersDao.getOrdersByUser(userId));
    }

    @Override
    public List<Order> getOrdersByProduct(String productId) {
        return constructOrder(ordersDao.getOrdersByProduct(productId));
    }

    @Override
    public Order getOrderById(String id) {
        return constructOrder(ordersDao.getOrdersById(id));
    }

    @Override
    public boolean createOrder(OrderRequest order) {
        OrderEntity entity = new OrderEntity();
        entity.setProductQtyMap(order.getProductQty());
        entity.setUserId(order.getUserId());
        entity.setAddress(order.getAddress());
        return ordersDao.addOrder(entity);
    }

    @Override
    public boolean cancelOrder(String orderId) {
        return ordersDao.cancelOrder(orderId);
    }

    @Override
    public boolean addProduct(String productId, Integer quantity, String orderId) {
        return ordersDao.addProduct(productId, quantity, orderId);
    }

    private List<Order> constructOrder(List<OrderEntity> orderEntityList) {
        List<Order> orders = new ArrayList<>(orderEntityList.size());
        Map<Integer, User> userMap = interaction.getUserDetails(null);
        Map<Integer, Product> productMap = interaction.getProductDetails(null);
        orderEntityList.stream().forEach(s -> {
            Order order = new Order();
            order.setUser(userMap.get(s.getUserId()));
            Map<Product, Integer> prodQtyMap = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : s.getProductQtyMap().entrySet()) {
                prodQtyMap.put(productMap.get(entry.getKey()), entry.getValue());
            }
            order.setProducts(prodQtyMap);
            //TODO: Set Logic for address
            order.setAddress(null);
            orders.add(order);
        });
        return orders;
    }

    private Order constructOrder(OrderEntity entity) {
        Map<Integer, User> userMap = interaction.getUserDetails(null);
        Map<Integer, Product> productMap = interaction.getProductDetails(null);
        Order order = new Order();
        order.setUser(userMap.get(entity.getUserId()));
        Map<Product, Integer> prodQtyMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : entity.getProductQtyMap().entrySet()) {
            prodQtyMap.put(productMap.get(entry.getKey()), entry.getValue());
        }
        order.setProducts(prodQtyMap);
        return order;
    }
}
