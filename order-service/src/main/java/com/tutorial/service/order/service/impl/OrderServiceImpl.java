package com.tutorial.service.order.service.impl;

import com.tutorial.commons.model.Order;
import com.tutorial.commons.model.Product;
import com.tutorial.commons.model.User;
import com.tutorial.service.order.dao.OrdersDao;
import com.tutorial.service.order.dao.entity.OrderEntity;
import com.tutorial.service.order.interaction.RestInteraction;
import com.tutorial.service.order.request.AddOrderRequest;
import com.tutorial.service.order.request.AddProductRequest;
import com.tutorial.service.order.service.OrdersService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public boolean createOrder(AddOrderRequest order) {
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
    public boolean addProduct(AddProductRequest request) {
        return ordersDao.addProduct(request.getProductId(), request.getProductQty(), request.getOrderId(), request.getUserId());
    }

    /**
     * Utility method to create multiple instances of @{@link Order} from corresponding @{@link OrderEntity}
     *
     * @param orderEntityList ArrayList of order entity
     * @return ArrayList of order
     */
    private List<Order> constructOrder(List<OrderEntity> orderEntityList) {
        List<Order> orders = new ArrayList<>(orderEntityList.size());
        if (CollectionUtils.isEmpty(orderEntityList)) {
            return orders;
        }
        Map<Integer, User> userMap = interaction.getUserDetails(extractUserIds.apply(orderEntityList));
        Map<Integer, Product> productMap = interaction.getProductDetails(extractProductIds(orderEntityList));

        orderEntityList.forEach(s -> {
            Order order = new Order();
            order.setId(s.getOrderId());
            order.setUser(userMap.get(s.getUserId()));
            List<Product> products = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : s.getProductQtyMap().entrySet()) {
                Product product = productMap.get(Integer.parseInt(entry.getKey()));
                for (int i = 0; i < entry.getValue(); i++) {
                    products.add(product);
                }
            }
            order.setProducts(products);
            order.setAddress(s.getAddress());
            calculateCost(order);
            orders.add(order);
        });
        return orders;
    }

    /**
     * Utility method to create an {@link Order} from an {@link OrderEntity}
     *
     * @param entity instance of an {@link OrderEntity}
     * @return an instance of {@link Order}
     */
    private Order constructOrder(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        Map<Integer, User> userMap = interaction.getUserDetails(extractUserIds.apply(Collections.singletonList(entity)));
        Map<Integer, Product> productMap = interaction.getProductDetails(extractProductIds(Collections.singletonList(entity)));
        Order order = new Order();
        order.setId(entity.getOrderId());
        order.setUser(userMap.get(entity.getUserId()));
        List<Product> products = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entity.getProductQtyMap().entrySet()) {
            Product product = productMap.get(Integer.parseInt(entry.getKey()));
            for (int i = 0; i < entry.getValue(); i++) {
                products.add(product);
            }
        }

        order.setProducts(products);
        order.setAddress(entity.getAddress());
        calculateCost(order);
        return order;
    }

    /**
     * Utility method to set the total cost for an order.
     *
     * @param order order for which the cost is to be calculated
     */
    private void calculateCost(Order order) {
        double cost = 0.0;
        for (Product product : order.getProducts()) {
            cost += product.getPrice();
        }
        order.setCost(cost);
    }

    /**
     * Extracts userIds from a list of {@link OrderEntity}
     */
    private Function<List<OrderEntity>, Set<Integer>> extractUserIds = o -> o.stream().map(OrderEntity::getUserId)
            .collect(Collectors.toSet());

    /**
     * Extracts productIds from a list of {@link OrderEntity}
     */
    private Set<Integer> extractProductIds(List<OrderEntity> orderEntities) {
        Set<Integer> productIds = new HashSet<>();
        for (OrderEntity entity : orderEntities) {
            Set<String> ids = entity.getProductQtyMap().keySet();
            ids.forEach(s -> {
                Integer extractedId = Integer.parseInt(s);
                productIds.add(extractedId);
            });
        }
        return productIds;
    }
}
