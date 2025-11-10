package com.example.mall.repository;

import com.example.mall.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    Page<Product> findByBrand(String brand, Pageable pageable);
    
    Page<Product> findByCategory(String category, Pageable pageable);
    
    Page<Product> findByBrandAndCategory(String brand, String category, Pageable pageable);
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findByBrand(String brand);
    
    List<Product> findByCategory(String category);
    
    @Query("SELECT DISTINCT p.brand FROM Product p")
    List<String> findDistinctBrands();
    
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}