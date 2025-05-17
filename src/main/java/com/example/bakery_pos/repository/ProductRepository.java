package com.example.bakery_pos.repository;

import com.example.bakery_pos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
<<<<<<< HEAD

    // Find products by category (case-insensitive for better UX)
    List<Product> findByCategoryIgnoreCase(String category);

    // Optional: Find products by name (partial match, case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Optional: Find all products sorted by price (ascending)
    List<Product> findAllByOrderByPriceAsc();

    // Optional: Find all products sorted by name (ascending)
    List<Product> findAllByOrderByNameAsc();
}
=======
    List<Product> findByCategory(String category);
    List<Product> findAll(); // To get all products initially
}
>>>>>>> f3685ca3c64026ae8f7165bcffdf7a540b04967c
