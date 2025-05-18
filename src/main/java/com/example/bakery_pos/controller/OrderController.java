package com.example.bakery_pos.controller;

import com.example.bakery_pos.dto.OrderRequest;
import com.example.bakery_pos.entity.Order;
import com.example.bakery_pos.repository.OrderRepository;
import com.example.bakery_pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order savedOrder = orderService.placeOrder(orderRequest);
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("Error placing order: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Order>> getRecentOrders() {
        try {
            List<Order> recentOrders = orderService.getRecentOrders();
            return new ResponseEntity<>(recentOrders, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching recent orders: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        try {
            Optional<Order> order = orderService.getOrderById(orderId);
            return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            System.err.println("Error fetching order by ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Order>> getPendingOrders() {
        try {
            List<Order> pendingOrders = orderService.getPendingOrders();
            return new ResponseEntity<>(pendingOrders, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching pending orders: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sales/daily")
    public ResponseEntity<Map<String, Object>> getDailySales() {
        try {
            Map<String, Object> dailySales = orderService.getDailySales();
            return ResponseEntity.ok(dailySales);
        } catch (Exception e) {
            System.err.println("Error fetching daily sales: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/sales/range")
    public ResponseEntity<List<Map<String, Object>>> getSalesForDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Map<String, Object>> salesData = orderService.getSalesForDateRange(startDate, endDate);
            return ResponseEntity.ok(salesData);
        } catch (Exception e) {
            System.err.println("Error fetching sales for date range: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<?> processPayment(@PathVariable Long orderId, @RequestBody Map<String, Object> paymentData) {
        try {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            Order order = optionalOrder.get();
            order.setStatus("DELIVERED");
            // Optionally process payment details from paymentData here

            orderRepository.save(order);
            return ResponseEntity.ok("Payment processed successfully. Order status updated to DELIVERED.");
        } catch (Exception e) {
            System.err.println("Error processing payment: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sales/trend/today")
    public ResponseEntity<List<Map<String, Object>>> getTodaySalesTrend() {
        try {
            List<Map<String, Object>> salesTrend = orderService.getTodaySalesTrend();
            return ResponseEntity.ok(salesTrend);
        } catch (Exception e) {
            System.err.println("Error fetching today's sales trend: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

