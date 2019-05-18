package com.tutorial.service.order.util;

import com.tutorial.commons.exceptions.BadInputException;
import com.tutorial.commons.model.Address;
import com.tutorial.service.order.request.AddOrderRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Input Validator that validates the input parameters for the Orders service.
 */
@Component
public class OrdersValidator {

    private static final String PIN_CODE_REGEX = "[0-9]{6}";

    public void validateProductId(String productId) {
        if (checkNonEmptyAndNumeric.test(productId))
            throw new BadInputException("Product Id should not be null");
    }

    public void validateUserId(String userId) {
        if (checkNonEmptyAndNumeric.test(userId)) {
            throw new BadInputException("User Id should not be null");
        }
    }

    public void validateOrderId(String orderId) {
        if (checkNonEmptyAndNumeric.test(orderId)) {
            throw new BadInputException("Order Id should not be null");
        }
    }

    public void validateCreateOrderRequest(AddOrderRequest order) {
        if (invalidAddressPredicate.test(order.getAddress())) {
            throw new BadInputException("Please pass a valid address.");
        }
        if (!validateProductMap.test(order.getProductQty())) {
            throw new BadInputException("Please pass valid product id and quantity");
        }
    }

    private Predicate<String> checkNonEmptyAndNumeric = s -> StringUtils.isBlank(s) || !StringUtils.isNumeric(s);

    private Predicate<Address> invalidAddressPredicate = s -> s == null || StringUtils.isBlank(s.getAddressLine()) || StringUtils.isBlank(s.getCity()) ||
            StringUtils.isBlank(s.getState()) || !String.valueOf(s.getPinCode()).matches(PIN_CODE_REGEX);

    private Predicate<Map<String, Integer>> validateProductMap = s -> {
        if (s == null) {
            return false;
        }
        for (Map.Entry<String, Integer> entry : s.entrySet()) {
            if (!StringUtils.isNumeric(entry.getKey()) || Integer.parseInt(entry.getKey()) < 1 || entry.getValue() < 1) {
                return false;
            }
        }
        return true;
    };
}
