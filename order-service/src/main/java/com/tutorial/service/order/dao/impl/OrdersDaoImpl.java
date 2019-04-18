package com.tutorial.service.order.dao.impl;

import com.tutorial.commons.annotations.DaoProfiler;
import com.tutorial.commons.model.Address;
import com.tutorial.commons.utils.QueryProvider;
import com.tutorial.service.order.dao.OrdersDao;
import com.tutorial.service.order.dao.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.tutorial.service.order.util.OrderConstants.*;

/**
 * Base implementation for @{@link OrdersDao}
 */
@Repository
public class OrdersDaoImpl implements OrdersDao {

    private QueryProvider queryProvider;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    public OrdersDaoImpl(QueryProvider queryProvider, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.queryProvider = queryProvider;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @DaoProfiler(queryName = "get-all-orders")
    public List<OrderEntity> getAllOrders() {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ALL_ORDERS);
        return namedParameterJdbcTemplate.query(query, (rs, row) -> mapOrder(rs));
    }

    @Override
    @DaoProfiler("get-orders-by-user")
    public List<OrderEntity> getOrdersByUser(String userId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ORDERS_BY_USER);
        List<OrderEntity> orderEntityList = new ArrayList<>();
        Map<String, String> params = new HashMap<>(1);
        params.put(USER_ID_PARAM, userId);
        namedParameterJdbcTemplate.query(query, params, rs -> {
            while (rs.next()) {
                orderEntityList.add(mapOrder(rs));
            }
        });
        return orderEntityList;
    }

    @Override
    @DaoProfiler("get-orders-by-product")
    public List<OrderEntity> getOrdersByProduct(String productId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ORDERS_BY_PRODUCT);
        List<OrderEntity> orderEntityList = new ArrayList<>();
        Map<String, String> params = new HashMap<>(1);
        params.put(PRODUCT_ID_PARAM, productId);
        namedParameterJdbcTemplate.query(query, params, rs -> {
            while (rs.next()) {
                orderEntityList.add(mapOrder(rs));
            }
        });
        return orderEntityList;
    }

    @Override
    @DaoProfiler("get-order-by-orderId")
    public OrderEntity getOrdersById(String orderId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ORDER_BY_ID);
        Map<String, String> params = new HashMap<>(1);
        params.put(ORDER_ID_PARAM, orderId);
        OrderEntity entity = namedParameterJdbcTemplate.query(query, rs -> rs.next() ? mapOrder(rs) : null);
        return entity;
    }

    @Override
    @DaoProfiler("create-new-order")
    public boolean addOrder(OrderEntity order) {
        String query = queryProvider.getTemplateQuery(QueryProvider.ADD_NEW_ORDER);
        Map<String, String> params[] = new HashMap[order.getProductQtyMap().size()];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : order.getProductQtyMap().entrySet()) {
            params[index].put(USER_ID_PARAM, String.valueOf(order.getUserId()));
            params[index].put(PRODUCT_ID, String.valueOf(entry.getKey()));
            params[index].put(PRODUCT_QTY_PARAM, String.valueOf(entry.getValue()));
        }
        //TODO: Check address and status update logic
        return namedParameterJdbcTemplate.batchUpdate(query, params).length != 0;
    }

    @Override
    @DaoProfiler("add-product-to-order")
    public boolean addProduct(String productId, Integer quantity, String orderId) {
        Future<Boolean> checkOrderResult = executorService.submit(() -> checkOrderExists(orderId));
        String query = queryProvider.getTemplateQuery(queryProvider.getTemplateQuery(QueryProvider.ADD_PRODUCT_TO_ORDER));
        Map<String, String> params = new HashMap<>(3);
        params.put(PRODUCT_ID_PARAM, productId);
        params.put(PRODUCT_QTY_PARAM, String.valueOf(quantity));
        params.put(ORDER_ID_PARAM, orderId);
        try {
            if (checkOrderResult.get())
                return namedParameterJdbcTemplate.update(query, params) != 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @DaoProfiler("cancel-order")
    public boolean cancelOrder(String orderId) {
        Future<Boolean> checkOrderResult = executorService.submit(() -> checkOrderExists(orderId));
        String query = queryProvider.getTemplateQuery(QueryProvider.CANCEL_ORDER);
        Map<String, String> params = new HashMap<>();
        params.put(ORDER_ID_PARAM, orderId);
        try {
            if (checkOrderResult.get()) {
                return namedParameterJdbcTemplate.update(query, params) != 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Utility method to check if an order with a given orderId exists.
     *
     * @param orderId Id of the order to be checked
     * @return true if the order already exists in the database, else false.
     */
    @DaoProfiler("check-order")
    private boolean checkOrderExists(String orderId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ORDER_COUNT);
        Map<String, String> params = new HashMap<>(1);
        params.put(ORDER_ID_PARAM, orderId);
        return namedParameterJdbcTemplate.query(query, params, rs -> {
            rs.next();
            return rs.getInt(1) > 0;
        });
    }

    private OrderEntity mapOrder(ResultSet rs) throws SQLException {
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(rs.getInt(ORDER_ID));
        entity.setUserId(rs.getInt(USER_ID));
        Map<Integer, Integer> qtyMap = new HashMap<>(1);
        qtyMap.put(rs.getInt(PRODUCT_ID), rs.getInt(PRODUCT_QTY));
        entity.setProductQtyMap(qtyMap);
        return entity;
    }
}
