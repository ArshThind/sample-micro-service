package com.tutorial.service.order.request;

import com.tutorial.commons.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OrderRequest {

    private int userId;

    private Address address;

    private Map<Integer, Integer> productQty;
}
