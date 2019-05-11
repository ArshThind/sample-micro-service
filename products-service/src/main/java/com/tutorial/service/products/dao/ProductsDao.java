package com.tutorial.service.products.dao;

import com.tutorial.commons.model.Product;

import java.util.List;
import java.util.Set;

/**
 * Base interface to implement @{@link Product} operations on the underlying database.
 */
public interface ProductsDao {

    /**
     * Method to retrieve all products from the underlying database
     *
     * @return list of all products present in the db.
     */
    List<Product> getAllProducts();

    /**
     * Method to retrieve product by ID from the underlying database
     *
     * @param productId id of the product
     * @return product with the given product id, if present in the db, else null
     */
    Product getProductById(String productId);

    /**
     * Method to retrieve product by category from the underlying database
     *
     * @param category
     * @return
     */
    List<Product> getProductsByCategory(String category);

    /**
     * Method to add new product in the underlying database
     *
     * @param product product to be added in the database
     * @return true/false depending on whether the product was successfully added or not.
     */
    boolean addNewProduct(Product product);

    /**
     * Method to remove the product from the underlying database
     *
     * @param productId id of the product to be removed from the database.
     * @return true/false depending on whether thr product removal was successful or not.
     */
    boolean removeProduct(String productId);

    /**
     * Retrieves multiple products from the database.
     *
     * @param productIds ids of the products to be retrieved from the database.
     * @return list of products with the given ids.
     */
    List<Product> getProductsByIdSet(Set<String> productIds);
}
