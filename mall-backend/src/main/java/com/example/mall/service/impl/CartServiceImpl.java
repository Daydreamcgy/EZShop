package com.example.mall.service.impl;

import com.example.mall.dto.CartItemDTO;
import com.example.mall.model.CartItem;
import com.example.mall.model.Product;
import com.example.mall.model.User;
import com.example.mall.repository.CartItemRepository;
import com.example.mall.repository.ProductRepository;
import com.example.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Override
    public List<CartItemDTO> getCartItemsAsDTO(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        return cartItems.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CartItem addProductToCart(User user, Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItemDTO addProductToCartAsDTO(User user, Long productId, Integer quantity) {
        CartItem cartItem = addProductToCart(user, productId, quantity);
        return convertToDTO(cartItem);
    }

    @Override
    public CartItem updateCartItemQuantity(User user, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Cart item does not belong to the user");
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItemDTO updateCartItemQuantityAsDTO(User user, Long cartItemId, Integer quantity) {
        CartItem cartItem = updateCartItemQuantity(user, cartItemId, quantity);
        return convertToDTO(cartItem);
    }

    @Override
    public void removeCartItem(User user, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Cart item does not belong to the user");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }

    private CartItemDTO convertToDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setQuantity(cartItem.getQuantity());
        
        Product product = cartItem.getProduct();
        if (product != null) {
            dto.setProductId(product.getId());
            dto.setProductName(product.getName());
            dto.setProductImage(product.getImageUrl());
            dto.setProductPrice(product.getPrice());
            dto.setProductStock(product.getStock());
        }
        
        return dto;
    }
}