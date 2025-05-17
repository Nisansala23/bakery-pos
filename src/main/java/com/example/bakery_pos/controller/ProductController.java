package com.example.bakery_pos.controller;

import com.example.bakery_pos.entity.Product;
import com.example.bakery_pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // Allow frontend requests from React dev server
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // GET /api/products — Fetch all products
    @GetMapping(value = { "", "/" })
=======
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products") // Base path for product-related APIs
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
>>>>>>> f3685ca3c64026ae8f7165bcffdf7a540b04967c
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

<<<<<<< HEAD
    // GET /api/products/category/{category} — Fetch products by category
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }
}
=======
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }
}
>>>>>>> f3685ca3c64026ae8f7165bcffdf7a540b04967c
