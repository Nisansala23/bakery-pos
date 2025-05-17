package com.example.bakery_pos.controller;

import com.example.bakery_pos.entity.Product;
import com.example.bakery_pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET /api/products/category/{category} — Fetch products by category
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }
}
