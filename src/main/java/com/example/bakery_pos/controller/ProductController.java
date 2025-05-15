package com.example.bakery_pos.controller;

import com.example.bakery_pos.entity.Product;
import com.example.bakery_pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }
}