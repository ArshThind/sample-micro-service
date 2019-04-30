package com.tutorial.service.products.dao;

import com.tutorial.commons.model.Product;

import java.io.IOException;
import java.util.List;

/**
 * Base interface to implement @{@link Product} operations on the underlying database.
 */
public interface ProductsDao {

    /**
     * Method to retrieve all products from the underlying database
     */

    List<Product> getAllProducts() throws IOException;

    /**
     * Method to retrieve product by ID from the underlying database
     * @param productId
     * @return
     * @throws IOException
     */

    Product getProductId(long productId) throws IOException;

    /**
     * Method to retrieve product by category from the underlying database
     * @param category
     * @return
     * @throws IOException
     */

    List<Product> getProductsByCategory(String category) throws IOException;

    /**
     * Method to add new product in the underlying database
     * @param product
     * @return
     * @throws IOException
     */

    boolean addNewProduct(Product product) throws IOException;

    /**
     * Method to add multiple products in the underlying database
     * @param products
     * @return
     * @throws IOException
     */

    boolean addMultipleProduct(List<Product> products) throws IOException;

    /**
     * Method to remove the product from the underlying database
     * @param product
     * @return
     * @throws IOException
     */
    boolean removeProduct(Product product) throws IOException;

}
