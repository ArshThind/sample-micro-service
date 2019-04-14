package com.tutorial.service.order.util;

import com.tutorial.commons.model.Order;
import com.tutorial.commons.utils.InputEntityValidator;
import org.springframework.stereotype.Service;

/**
 * Input Validator that validates the input parameters for the Orders service.
 */
@Service
public class OrdersValidator extends InputEntityValidator<Order> {

    /**
     * {@inheritDoc}
     *
     * @param order
     * @return
     */
    @Override
    protected boolean validateOptionalParameters(Order order) {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     * @return
     */
    @Override
    protected boolean validateMandatoryParameters(Order order) {
        return true;
    }
}
