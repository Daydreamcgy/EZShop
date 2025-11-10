package com.example.mall.service;

import com.example.mall.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Page<Product> getProducts(Pageable pageable);
    List<Product> getProductsByName(String name);
    Page<Product> getProductsByName(String name, Pageable pageable);
    List<Product> getProductsByBrand(String brand);
    Page<Product> getProductsByBrand(String brand, Pageable pageable);
    List<Product> getProductsByCategory(String category);
    Page<Product> getProductsByCategory(String category, Pageable pageable);
    Page<Product> getProductsByBrandAndCategory(String brand, String category, Pageable pageable);
    List<String> getAllBrands();
    List<String> getAllCategories();
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}