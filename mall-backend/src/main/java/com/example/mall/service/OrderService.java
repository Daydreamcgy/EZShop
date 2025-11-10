package com.example.mall.service;

import com.example.mall.model.Order;
import com.example.mall.model.User;
import java.util.List;

public interface OrderService {
    Order createOrder(User user);
    Order getOrderById(Long id);
    List<Order> getOrdersByUser(User user);
    Order updateOrderStatus(Long orderId, String status);
}