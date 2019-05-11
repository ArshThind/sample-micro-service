package com.tutorial.service.products.util;

import com.tutorial.commons.exceptions.BadInputException;
import com.tutorial.commons.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

/**
 * Utility to validate the inputs passed to the products service.
 */
@Component
public class ProductsValidator {

    public int validateGetProductRequest(String productIds, String category) {
        if (getAllProductsRequest(productIds, category)) {
            return 1;
        }
        if (!StringUtils.isAnyEmpty(productIds, category)) {
            throw new BadInputException("Request should contain either productIds or category but not both");
        }
        return StringUtils.isEmpty(category) ? 2 : 3;
    }

    public void validateProductId(String productId) {
        if (testNonEmpty.test(productId) && StringUtils.isNumeric(productId)) {
            return;
        }
        throw new BadInputException("Please pass a valid productId");
    }

    public void validateAddProductRequest(Product product) {
        if (product == null || product.getPrice() <= 0
                || product.getAvailableQuantity() <= 0
                || !testNonEmpty.test(product.getDescription())) {
            throw new BadInputException("Please pass a valid product.");
        }
    }

    private boolean getAllProductsRequest(String productIds, String category) {
        return StringUtils.isAllEmpty(productIds, category);
    }

    private Predicate<String> testNonEmpty = StringUtils::isNotEmpty;

}
