package com.example.mall.service;

import com.example.mall.dto.CartItemDTO;
import com.example.mall.model.CartItem;
import com.example.mall.model.User;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(User user);
    List<CartItemDTO> getCartItemsAsDTO(User user);
    CartItem addProductToCart(User user, Long productId, Integer quantity);
    CartItemDTO addProductToCartAsDTO(User user, Long productId, Integer quantity);
    CartItem updateCartItemQuantity(User user, Long cartItemId, Integer quantity);
    CartItemDTO updateCartItemQuantityAsDTO(User user, Long cartItemId, Integer quantity);
    void removeCartItem(User user, Long cartItemId);
    void clearCart(User user);
}