package com.tutorial.service.products.service.impl;

import com.tutorial.commons.model.Product;
import com.tutorial.service.products.dao.ProductsDao;
import com.tutorial.service.products.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService {

    private ProductsDao productsDao;

    @Autowired
    public ProductsServiceImpl (ProductsDao productsDao) { this.productsDao = productsDao; }


    @Override
    public List<Product> getAllProducts() throws IOException {
        return null;
    }

    @Override
    public Product getProductById(long productId) throws IOException {
        return null;
    }

    @Override
    public Product getProductByCategory(String category) throws IOException {
        return null;
    }

    @Override
    public boolean addNewProduct(Product product) throws IOException {
        return false;
    }


    @Override
    public boolean removeProduct(Product product) throws IOException {
        return false;
    }
}
