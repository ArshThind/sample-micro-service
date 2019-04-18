package com.tutorial.service.products.controller;

import com.tutorial.commons.model.Product;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Spring REST controller that provides api endpoints to perform operations on @{@link Product}
 */
@RestController
@RequestMapping("/products")
public class ProductsController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getAllProducts() {
        return null;
    }

    @RequestMapping(value = "/{category}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<Product> getProductsByCategory(@PathVariable("category") String category) {
        return null;
    }

    @RequestMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void addNewProduct(Product product) {
        return;
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void addMultipleProducts(List<Product> product) {
        return;
    }

    @RequestMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public void deleteProduct(Product product) {
        return;
    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public void deleteMultipleProducts(List<Product> products) {
        return;
    }
}
