package com.example.mall.controller;

import com.example.mall.dto.CartItemDTO;
import com.example.mall.model.CartItem;
import com.example.mall.model.User;
import com.example.mall.payload.AddToCartRequest;
import com.example.mall.payload.UpdateQuantityRequest;
import com.example.mall.service.CartService;
import com.example.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userService.findByUsername(userDetails.getUsername());
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems() {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        List<CartItemDTO> cartItems = cartService.getCartItemsAsDTO(user);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemDTO> addProductToCart(@RequestBody AddToCartRequest request) {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        CartItemDTO cartItem = cartService.addProductToCartAsDTO(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cartItem);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CartItemDTO> updateCartItemQuantity(@PathVariable Long id, @RequestBody UpdateQuantityRequest request) {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        CartItemDTO cartItem = cartService.updateCartItemQuantityAsDTO(user, id, request.getQuantity());
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long id) {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        cartService.removeCartItem(user, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        cartService.clearCart(user);
        return ResponseEntity.ok().build();
    }
}