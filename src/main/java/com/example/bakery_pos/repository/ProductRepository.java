package com.example.bakery_pos.repository;

import com.example.bakery_pos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by category (case-insensitive for better UX)
    List<Product> findByCategoryIgnoreCase(String category);

    // Find products by partial name match (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Sort all products by price ascending
    List<Product> findAllByOrderByPriceAsc();

    // Sort all products by name ascending
    List<Product> findAllByOrderByNameAsc();

    // Optionally include for clarity, though findAll() exists by default
    List<Product> findAll();
}
