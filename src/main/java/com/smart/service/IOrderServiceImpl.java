package com.smart.service;

import com.smart.dao.OrderRepository;
import com.smart.model.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IOrderServiceImpl implements IOrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Orders saveOrder(Orders orders) {
        return orderRepository.save(orders);
    }

    @Override
    public Orders getOrder(String orderId) {
        return orderRepository.getOrdersByOrderId(orderId);
    }
}
