package com.tutorial.service.products.controller;

import com.tutorial.commons.exceptions.BadInputException;
import com.tutorial.commons.model.Product;
import com.tutorial.service.products.service.ProductsService;
import com.tutorial.service.products.util.ProductsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Spring REST controller that provides api endpoints to perform operations on {@link Product}
 */
@RestController
@RequestMapping("/products")
@Slf4j
public class ProductsController {

    private ProductsService productsService;

    private ProductsValidator validator;

    @Autowired
    public ProductsController(ProductsService productsService, ProductsValidator validator) {
        this.productsService = productsService;
        this.validator = validator;
    }

    /**
     * Provides REST endpoint to retrieve products registered with the service.
     * By default, retrieves all the registered products, but can be filtered based on the productIds or category.
     *
     * @param productIds (Optional) comma separated productIds
     * @param category   (Optional) category of the product
     * @return list of products if present, else returns HTTP status 204.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProducts(String productIds, String category) {
        try {
            int requestType = validator.validateGetProductRequest(productIds, category);
            List<Product> products;
            switch (requestType) {
                case 1:
                    products = productsService.getAllProducts();
                    break;
                case 2:
                    products = productsService.getProductsByIdSet(productIds);
                    break;
                case 3:
                    products = productsService.getProductsByCategory(category);
                    break;
                default:
                    throw new WebApplicationException(Response.Status.NO_CONTENT);

            }
            if (products.size() > 0) {
                return products;
            }
        } catch (BadInputException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while execution: {}", e);
            throw new WebApplicationException("Error! Service Unavailable.", Response.Status.INTERNAL_SERVER_ERROR);
        }
        throw new WebApplicationException(Response.Status.NO_CONTENT);
    }

    /**
     * REST end point to retrieve a product based on the productId.
     *
     * @param productId id of the product.
     * @return Product with the given id if present, else returns HTTP status 204.
     */
    @RequestMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Product getProductsById(@PathVariable("productId") String productId) {
        try {
            validator.validateProductId(productId);
            Product product = productsService.getProductById(productId);
            if (productId == null) {
                throw new WebApplicationException(Response.Status.NO_CONTENT);
            }
            return product;
        } catch (BadInputException bie) {
            throw bie;
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while execution: {}", e);
            throw new WebApplicationException("Error! Service Unavailable", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REST end point to register a new product with the service.
     *
     * @param product Product to be added.
     * @return Http status 200 if operation successful, else returns Http status 417.
     */
    @RequestMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public Response addNewProduct(Product product) {
        try {
            validator.validateAddProductRequest(product);
            if (productsService.addNewProduct(product)) {
                return Response.ok().build();
            }
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        } catch (BadInputException bie) {
            throw bie;
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while execution: {}", e);
            throw new WebApplicationException("Error! Service Unavailable", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * REST end point to remove a product from the service.
     *
     * @param productId id of the product to be removed.
     * @return Http status 200 if operation successful, else returns Http status 417.
     */
    @RequestMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    public Response deleteProduct(String productId) {
        try {
            validator.validateProductId(productId);
            if (productsService.removeProduct(productId)) {
                return Response.ok().build();
            }
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        } catch (BadInputException bie) {
            throw bie;
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while execution: {}", e);
            throw new WebApplicationException("Error! Service Unavailable", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
