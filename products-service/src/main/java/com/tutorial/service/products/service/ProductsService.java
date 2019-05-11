package com.tutorial.service.products.service;

import com.tutorial.commons.model.Product;

import java.io.IOException;
import java.util.List;

public interface ProductsService {

    List<Product> getAllProducts() throws IOException;

    Product getProductById(String productId) throws IOException;

    List<Product> getProductsByCategory(String category) throws IOException;

    boolean addNewProduct(Product product) throws IOException;

    boolean removeProduct(Product product) throws IOException;

    List<Product> getProductsByIds(String productIds) throws IOException;
}
