package com.example.mall.service.impl;

import com.example.mall.model.*;
import com.example.mall.repository.OrderRepository;
import com.example.mall.repository.CartItemRepository;
import com.example.mall.service.OrderService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Order createOrder(User user) {
        // 获取用户购物车项
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空，无法创建订单");
        }

        // 创建订单项
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.calculateSubtotal(); // 计算小计
            orderItems.add(orderItem);
        }

        // 计算总金额
        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 创建订单
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        // 保存订单
        Order savedOrder = orderRepository.save(order);

        // 清空购物车
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            try {
                order.setStatus(OrderStatus.valueOf(status));
                order.setUpdatedAt(new Date());
                return orderRepository.save(order);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("无效的订单状态: " + status);
            }
        }
        return null;
    }

    @Override
    public Workbook exportOrdersToExcel(User user) throws IOException {
        // 获取用户的所有订单
        List<Order> orders = orderRepository.findByUser(user);
        
        // 创建新的Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        
        // 创建工作表
        Sheet sheet = workbook.createSheet("订单记录");
        
        // 设置列宽
        sheet.setColumnWidth(0, 15 * 256); // 订单ID
        sheet.setColumnWidth(1, 20 * 256); // 订单状态
        sheet.setColumnWidth(2, 20 * 256); // 创建时间
        sheet.setColumnWidth(3, 20 * 256); // 总金额
        sheet.setColumnWidth(4, 30 * 256); // 商品信息
        sheet.setColumnWidth(5, 15 * 256); // 数量
        sheet.setColumnWidth(6, 15 * 256); // 单价
        sheet.setColumnWidth(7, 15 * 256); // 小计
        
        // 创建表头行
        Row headerRow = sheet.createRow(0);
        
        // 创建表头单元格样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // 设置表头内容
        String[] headers = {"订单ID", "订单状态", "创建时间", "总金额", "商品信息", "数量", "单价", "小计"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 日期格式化
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // 创建数据单元格样式
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // 金额单元格样式
        CellStyle currencyStyle = workbook.createCellStyle();
        // 复制dataStyle的边框和对齐属性
        currencyStyle.setBorderTop(dataStyle.getBorderTop());
        currencyStyle.setBorderBottom(dataStyle.getBorderBottom());
        currencyStyle.setBorderLeft(dataStyle.getBorderLeft());
        currencyStyle.setBorderRight(dataStyle.getBorderRight());
        currencyStyle.setVerticalAlignment(dataStyle.getVerticalAlignment());
        
        DataFormat format = workbook.createDataFormat();
        currencyStyle.setDataFormat(format.getFormat("¥#,##0.00"));
        
        // 填充数据行
        int rowNum = 1;
        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();
            if (orderItems != null && !orderItems.isEmpty()) {
                // 对每个订单项创建一行
                for (int i = 0; i < orderItems.size(); i++) {
                    OrderItem item = orderItems.get(i);
                    Row row = sheet.createRow(rowNum++);
                    
                    // 第一行显示订单信息，后续行只显示商品信息
                    if (i == 0) {
                        // 订单ID
                        Cell cell0 = row.createCell(0);
                        cell0.setCellValue(order.getId());
                        cell0.setCellStyle(dataStyle);
                        
                        // 订单状态
                        Cell cell1 = row.createCell(1);
                        cell1.setCellValue(getOrderStatusText(order.getStatus()));
                        cell1.setCellStyle(dataStyle);
                        
                        // 创建时间
                        Cell cell2 = row.createCell(2);
                        cell2.setCellValue(dateFormat.format(order.getCreatedAt()));
                        cell2.setCellStyle(dataStyle);
                        
                        // 总金额
                        Cell cell3 = row.createCell(3);
                        cell3.setCellValue(order.getTotalAmount().doubleValue());
                        cell3.setCellStyle(currencyStyle);
                    } else {
                        // 非第一行，保持前几列空白
                        for (int j = 0; j < 4; j++) {
                            Cell cell = row.createCell(j);
                            cell.setCellStyle(dataStyle);
                        }
                    }
                    
                    // 商品信息
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(item.getProduct().getName());
                    cell4.setCellStyle(dataStyle);
                    
                    // 数量
                    Cell cell5 = row.createCell(5);
                    cell5.setCellValue(item.getQuantity());
                    cell5.setCellStyle(dataStyle);
                    
                    // 单价
                    Cell cell6 = row.createCell(6);
                    cell6.setCellValue(item.getPrice().doubleValue());
                    cell6.setCellStyle(currencyStyle);
                    
                    // 小计
                    Cell cell7 = row.createCell(7);
                    cell7.setCellValue(item.getSubtotal().doubleValue());
                    cell7.setCellStyle(currencyStyle);
                }
            }
        }
        
        return workbook;
    }
    
    /**
     * 获取订单状态的中文文本
     */
    private String getOrderStatusText(OrderStatus status) {
        switch (status) {
            case PENDING_PAYMENT:
                return "待付款";
            case PAID:
                return "已付款";
            case SHIPPED:
                return "已发货";
            case DELIVERED:
                return "已送达";
            case COMPLETED:
                return "已完成";
            case CANCELLED:
                return "已取消";
            default:
                return status.name();
        }
    }
}