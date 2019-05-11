package com.tutorial.service.products.service.impl;

import com.tutorial.commons.model.Product;
import com.tutorial.service.products.dao.ProductsDao;
import com.tutorial.service.products.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base implementation for {@link ProductsService}
 */
@Service
public class ProductsServiceImpl implements ProductsService {

    private ProductsDao productsDao;

    @Autowired
    public ProductsServiceImpl (ProductsDao productsDao) { this.productsDao = productsDao; }


    @Override
    public List<Product> getAllProducts() { return this.productsDao.getAllProducts();}

    @Override
    public Product getProductById(String productId) { return this.productsDao.getProductById(productId); }

    @Override
    public List<Product> getProductsByCategory(String category) { return this.productsDao.getProductsByCategory(category); }

    @Override
    public boolean addNewProduct(Product product) { return this.productsDao.addNewProduct(product); }

    @Override
    public boolean removeProduct(String productId) { return this.productsDao.removeProduct(productId); }

    @Override
    public List<Product> getProductsByIdSet(String productIds) {
        Set<String> productIdSet = new HashSet<>();
        CollectionUtils.addAll(productIdSet, productIds.split(","));
        return this.productsDao.getProductsByIdSet(productIdSet);
    }
}
