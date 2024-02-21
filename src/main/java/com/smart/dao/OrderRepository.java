package com.smart.dao;

import com.smart.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders,Long> {

    public Orders getOrdersByOrderId(String orderId);
}
