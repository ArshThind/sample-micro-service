package com.tutorial.service.order.dao.entity;

import com.tutorial.commons.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OrderEntity {

    private int orderId;
    private int userId;
    private Map<Integer, Integer> productQtyMap;
    private Address address;
}
