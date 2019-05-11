package com.tutorial.service.products.service.impl;

import com.tutorial.commons.model.Product;
import com.tutorial.service.products.dao.ProductsDao;
import com.tutorial.service.products.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductsServiceImpl implements ProductsService {

    private ProductsDao productsDao;

    @Autowired
    public ProductsServiceImpl (ProductsDao productsDao) { this.productsDao = productsDao; }


    @Override
    public List<Product> getAllProducts() throws IOException { return this.productsDao.getAllProducts();}

    @Override
    public Product getProductById(String productId) throws IOException { return this.productsDao.getProductById(productId); }

    @Override
    public List<Product> getProductsByCategory(String category) throws IOException { return this.productsDao.getProductsByCategory(category); }

    @Override
    public boolean addNewProduct(Product product) throws IOException { return this.productsDao.addNewProduct(product); }

    @Override
    public boolean removeProduct(Product product) throws IOException { return this.productsDao.removeProduct(product); }

    @Override
    public List<Product> getProductsByIds(String productIds) throws IOException {
        Set<String> productIdSet = new HashSet<>();
        CollectionUtils.addAll(productIdSet, productIds.split(","));
        return this.productsDao.getProductById(productIdSet);
    }
}
