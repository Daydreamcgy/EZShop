package com.example.mall.controller;

import com.example.mall.model.Order;
import com.example.mall.model.User;
import com.example.mall.service.OrderService;
import com.example.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 创建订单
     */
    @PostMapping
    public ResponseEntity<?> createOrder() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("用户未认证");
            }
            
            String username;
            if (authentication.getPrincipal() instanceof UserDetails) {
                username = ((UserDetails) authentication.getPrincipal()).getUsername();
            } else {
                username = authentication.getPrincipal().toString();
            }
            
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(401).body("用户不存在");
            }
            
            Order order = orderService.createOrder(user);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根据ID获取订单
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).build();
            }
            
            Order order = orderService.getOrderById(id);
            if (order != null) {
                // 验证订单是否属于当前用户
                String username;
                if (authentication.getPrincipal() instanceof UserDetails) {
                    username = ((UserDetails) authentication.getPrincipal()).getUsername();
                } else {
                    username = authentication.getPrincipal().toString();
                }
                
                User currentUser = userService.findByUsername(username);
                if (!order.getUser().getId().equals(currentUser.getId())) {
                    return ResponseEntity.status(403).build();
                }
                
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取当前用户的所有订单
     */
    @GetMapping
    public ResponseEntity<List<Order>> getOrdersByUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).build();
            }
            
            String username;
            if (authentication.getPrincipal() instanceof UserDetails) {
                username = ((UserDetails) authentication.getPrincipal()).getUsername();
            } else {
                username = authentication.getPrincipal().toString();
            }
            
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(401).build();
            }
            
            List<Order> orders = orderService.getOrdersByUser(user);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("用户未认证");
            }
            
            Order order = orderService.updateOrderStatus(id, status);
            if (order != null) {
                // 验证订单是否属于当前用户
                String username;
                if (authentication.getPrincipal() instanceof UserDetails) {
                    username = ((UserDetails) authentication.getPrincipal()).getUsername();
                } else {
                    username = authentication.getPrincipal().toString();
                }
                
                User currentUser = userService.findByUsername(username);
                if (!order.getUser().getId().equals(currentUser.getId())) {
                    return ResponseEntity.status(403).body("无权限操作该订单");
                }
                
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}