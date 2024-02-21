package com.smart.service;

import com.smart.model.Orders;


public interface IOrderService {

    Orders saveOrder(Orders orders);

    Orders getOrder(String orderId);

}
