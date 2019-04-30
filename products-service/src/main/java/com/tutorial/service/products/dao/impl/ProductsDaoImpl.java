package com.tutorial.service.products.dao.impl;

import com.tutorial.commons.annotations.DaoProfiler;
import com.tutorial.commons.model.Product;
import com.tutorial.commons.utils.QueryProvider;
import com.tutorial.service.products.dao.ProductsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tutorial.service.products.configuration.Constants.*;

public class ProductsDaoImpl implements ProductsDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private QueryProvider queryProvider;

    /**
     * @param jdbcTemplate
     * @param queryProvider
     */

    @Autowired
    public ProductsDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, QueryProvider queryProvider) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
        this.queryProvider = queryProvider;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    @DaoProfiler(queryName = "get-all-products")
    public List<Product> getAllProducts() {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_ALL_PRODUCTS);
        List<Product> products = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, rs -> {
                    products.add(mapProduct(rs));
                }
        );
        return products;
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_PRODUCTS_BY_CATEGORY);
        Map<String, String> params = new HashMap<>();
        params.put(CATEGORY, category);
        List<Product> products = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            products.add(mapProduct(rs));
        });
        return products;
    }

    public Product getProductById(long productId) {
        String query = queryProvider.getTemplateQuery(QueryProvider.GET_PRODUCT_BY_ID);
        Map<String, String> params = new HashMap<>();
        params.put(PRODUCT_ID, Long.toString(productId));
        Product product = namedParameterJdbcTemplate.query(query, params, rs -> {
            if (rs.next()) {
                return mapProduct(rs);
            }
            return null;
        });
        return product;
    }

    @Override
    public boolean addNewProduct(Product product) throws IOException {
        String query = queryProvider.getTemplateQuery(QueryProvider.ADD_NEW_PRODUCT);
        Map<String, String> params = new HashMap<>();
        params.put(PRODUCT_NAME, product.getName());
        params.put(CATEGORY, product.getCategory());
        params.put(DESCRIPTION, product.getDescription());
        params.put(UNIT_PRICE, Double.toString(product.getPrice()));
        params.put(AVAIL_QTY, Integer.toString(product.getAvailableQuantity()));

        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    @Override
    public boolean addMultipleProduct(List<Product> products) throws IOException {
        return false;
    }

    @Override
    public boolean removeProduct(Product product) throws IOException {
        String query = queryProvider.getTemplateQuery(QueryProvider.REMOVE_PRODUCT);
        Map<String, String> params = new HashMap<>();
        params.put(PRODUCT_ID, Long.toString(product.getProductId()));

        return namedParameterJdbcTemplate.update(query, params) > 0;

    }


    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setName(rs.getString("product_name"));
        product.setProductId(rs.getLong("product_id"));
        product.setCategory(rs.getString("category"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("unit_price"));
        product.setAvailableQuantity(rs.getInt("avail_qty"));
        return product;
    }


}
