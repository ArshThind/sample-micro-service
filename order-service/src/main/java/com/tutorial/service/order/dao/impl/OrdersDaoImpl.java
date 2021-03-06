package com.tutorial.service.order.dao.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tutorial.commons.annotations.DaoProfiler;
import com.tutorial.commons.model.Address;
import com.tutorial.commons.utils.QueryProvider;
import com.tutorial.service.order.dao.OrdersDao;
import com.tutorial.service.order.dao.entity.OrderEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tutorial.service.order.util.OrderConstants.*;

/**
 * Base implementation for {@link OrdersDao}
 */
@Repository
public class OrdersDaoImpl implements OrdersDao {

    private QueryProvider queryProvider;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ExecutorService executorService;

    private ThreadFactory threadFactory;

    @Autowired
    public OrdersDaoImpl(QueryProvider queryProvider, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.queryProvider = queryProvider;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        threadFactory = createThreadFactory();
        executorService = Executors.newFixedThreadPool(10, threadFactory);
    }

    private ThreadFactory createThreadFactory() {
        return new ThreadFactoryBuilder()
                .setThreadFactory(Executors.defaultThreadFactory())
                .setNameFormat("Orders-Pool-%d")
                .build();
    }

    @Override
    @DaoProfiler(queryName = "get-all-orders")
    public List<OrderEntity> getAllOrders() {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ALL_ORDERS);
        List<OrderEntity> orders = namedParameterJdbcTemplate.query(query, (rs, row) -> mapOrder(rs));
        return aggregateOrders.apply(orders);
    }

    @Override
    @DaoProfiler(queryName = "get-orders-by-user")
    public List<OrderEntity> getOrdersByUser(String userId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ORDERS_BY_USER);
        List<OrderEntity> orderEntityList = new ArrayList<>();
        Map<String, String> params = new HashMap<>(1);
        params.put(USER_ID_PARAM, userId);
        namedParameterJdbcTemplate.query(query, params, rs -> {
            orderEntityList.add(mapOrder(rs));
        });
        return aggregateOrders.apply(orderEntityList);
    }

    @Override
    @DaoProfiler(queryName = "get-orders-by-product")
    public List<OrderEntity> getOrdersByProduct(String productId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ORDERS_BY_PRODUCT);
        List<OrderEntity> orderEntityList = new ArrayList<>();
        Map<String, String> params = new HashMap<>(1);
        params.put(PRODUCT_ID_PARAM, productId);
        namedParameterJdbcTemplate.query(query, params, rs -> {
            orderEntityList.add(mapOrder(rs));
        });
        return aggregateOrders.apply(orderEntityList);
    }

    @Override
    @DaoProfiler(queryName = "get-order-by-orderId")
    public OrderEntity getOrdersById(String orderId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ORDER_BY_ID);
        Map<String, String> params = new HashMap<>(1);
        params.put(ORDER_ID_PARAM, orderId);
        List<OrderEntity> orders = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            orders.add(mapOrder(rs));
        });
        return CollectionUtils.isEmpty(orders) ? null : aggregateOrders.apply(orders).get(0);
    }

    @Override
    @DaoProfiler(queryName = "create-new-order")
    public boolean addOrder(OrderEntity order) {
        String query = queryProvider.getTemplateQuery(QueryProvider.ADD_NEW_ORDER);
        Map<String, String> params[] = new HashMap[order.getProductQtyMap().size()];
        int index = 0;
        for (Map.Entry<String, Integer> entry : order.getProductQtyMap().entrySet()) {
            params[index] = new HashMap<>();
            params[index].put(USER_ID_PARAM, String.valueOf(order.getUserId()));
            params[index].put(PRODUCT_ID_PARAM, entry.getKey());
            params[index].put(PRODUCT_QTY_PARAM, String.valueOf(entry.getValue()));
            index++;
        }
        int[] records = namedParameterJdbcTemplate.batchUpdate(query, params);
        createAddressRow(order.getAddress());
        createStatusRow();
        return records.length > 0;
    }

    @Override
    @DaoProfiler(queryName = "add-product-to-order")
    public boolean addProduct(int productId, int quantity, int orderId, int userId) {
        Future<Boolean> checkOrderResult = executorService.submit(() -> checkOrderExists(String.valueOf(orderId)));
        String query = queryProvider.getTemplateQuery(QueryProvider.ADD_PRODUCT_TO_ORDER);
        Map<String, Integer> params = new HashMap<>(3);
        params.put(PRODUCT_ID_PARAM, productId);
        params.put(PRODUCT_QTY_PARAM, quantity);
        params.put(ORDER_ID_PARAM, orderId);
        params.put(USER_ID_PARAM, userId);
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
    @DaoProfiler(queryName = "cancel-order")
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
    @DaoProfiler(queryName = "check-order")
    private boolean checkOrderExists(String orderId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ORDER_COUNT);
        Map<String, String> params = new HashMap<>(1);
        params.put(ORDER_ID_PARAM, orderId);
        return namedParameterJdbcTemplate.query(query, params, rs -> {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        });
    }

    /**
     * Utility method to create @{@link OrderEntity} from a @{@link ResultSet}
     *
     * @param rs ResultSet obtained after querying the database
     * @return Instance of @{@link OrderEntity}
     * @throws SQLException Exception that provides information on database errors
     */
    private OrderEntity mapOrder(ResultSet rs) throws SQLException {
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(rs.getInt(ORDER_ID));
        entity.setUserId(rs.getInt(USER_ID));
        Map<String, Integer> qtyMap = new HashMap<>(1);
        qtyMap.put(rs.getString(PRODUCT_ID), rs.getInt(PRODUCT_QTY));
        entity.setProductQtyMap(qtyMap);
        Address address = new Address();
        address.setAddressLine(rs.getString(LINE_ONE));
        address.setCity(rs.getString(CITY));
        address.setPinCode(rs.getInt(PIN_CODE));
        address.setState(rs.getString(STATE));
        entity.setAddress(address);
        return entity;
    }

    /**
     * Utility method to create an order status row when creating a new order
     */
    @DaoProfiler(queryName = "create-status-row")
    private void createStatusRow() {
        String query = queryProvider.getTemplateQuery(QueryProvider.ADD_STATUS_ROW);
        namedParameterJdbcTemplate.update(query, new HashMap<>(0));
    }

    /**
     * Utility method to create an address row when creating a new order
     *
     * @param address Object containing the address information
     */
    @DaoProfiler(queryName = "add-address")
    private void createAddressRow(Address address) {
        String query = queryProvider.getTemplateQuery(QueryProvider.ADD_ADDRESS_ROW);
        Map<String, String> params = new HashMap<>();
        params.put(LINE_ONE_PARAM, address.getAddressLine());
        params.put(CITY, address.getCity());
        params.put(STATE, address.getState());
        params.put(PIN_CODE_PARAM, String.valueOf(address.getPinCode()));
        namedParameterJdbcTemplate.update(query, params);
    }

    /**
     * Utility function to aggregate all the products of an order into a single order.
     */
    private Function<List<OrderEntity>, List<OrderEntity>> aggregateOrders = o -> new ArrayList<>(o.stream()
            .collect(
                    Collectors.toMap
                            (s -> s.getOrderId(),
                                    s -> s,
                                    (s1, s2) -> {
                                        s1.getProductQtyMap().putAll(s2.getProductQtyMap());
                                        return s1;
                                    })).values());
}
