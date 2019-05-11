package com.tutorial.service.products.service;

import com.tutorial.commons.model.Product;

import java.util.List;

/**
 * Base interface defining the set of operations that can be performed on a {@link Product}
 */
public interface ProductsService {

    /**
     * Retrieves all products registered with the service.
     *
     * @return list of all products registered with the service.
     */
    List<Product> getAllProducts();

    /**
     * Retrieves the product with a particular id.
     *
     * @param productId id of the product.
     * @return product with the given id, else returns null
     */
    Product getProductById(String productId);

    /**
     * Retrieves products with a particular category.
     *
     * @param category category of the product
     * @return list of products with the given category if present, else empty list.
     */
    List<Product> getProductsByCategory(String category);

    /**
     * Registers a new product with the service.
     *
     * @param product product to be registered.
     * @return true/false indicating the product registration status.
     */
    boolean addNewProduct(Product product);

    /**
     * Un-registers a product from the service.
     *
     * @param productId id of the product to be registered.
     * @return true/false depending on whether the product was successfully un-registered or not.
     */
    boolean removeProduct(String productId);

    /**
     * Retrieves a list of products matching a given set of product ids.
     *
     * @param productIds ids of the products to be retrieved.
     * @return list of products with the given product ids.
     */
    List<Product> getProductsByIdSet(String productIds);
}
