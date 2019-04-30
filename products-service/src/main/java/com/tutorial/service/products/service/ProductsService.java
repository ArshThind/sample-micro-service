package com.tutorial.service.products.service;

import com.tutorial.commons.model.Product;

import java.io.IOException;
import java.util.List;

public interface ProductsService {

    List<Product> getAllProducts() throws IOException;

    Product getProductById(long productId) throws IOException;

    Product getProductByCategory(String category) throws IOException;

    boolean addNewProduct(Product product) throws IOException;

    boolean removeProduct(Product product) throws IOException;


}
