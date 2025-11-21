package com.example.mall.service;

import com.example.mall.model.Order;
import com.example.mall.model.User;
import org.apache.poi.ss.usermodel.Workbook;
import java.util.List;
import java.io.IOException;

public interface OrderService {
    Order createOrder(User user);
    Order getOrderById(Long id);
    List<Order> getOrdersByUser(User user);
    Order updateOrderStatus(Long orderId, String status);
    
    /**
     * 导出用户订单为Excel文件
     * @param user 用户
     * @return Excel工作簿对象
     * @throws IOException IO异常
     */
    Workbook exportOrdersToExcel(User user) throws IOException;
}