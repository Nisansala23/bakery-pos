package com.example.bakery_pos.controller;

import com.example.bakery_pos.dto.OrderRequest;
import com.example.bakery_pos.entity.Order;
import com.example.bakery_pos.entity.OrderItem;
import com.example.bakery_pos.repository.OrderItemRepository;
import com.example.bakery_pos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date; // Import java.sql.Date
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order order = new Order();
            order.setOrderDate(LocalDateTime.now(ZoneId.of("Asia/Colombo")));
            order.setTotalAmount(orderRequest.getTotalAmount());
            order.setStatus("PENDING"); // Initial status

            List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                    .map(itemDto -> {
                        OrderItem orderItem = new OrderItem();
                        orderItem.setOrder(order);
                        orderItem.setProductId(itemDto.getProductId());
                        orderItem.setQuantity(itemDto.getQuantity());
                        orderItem.setItemPrice(itemDto.getItemPrice());
                        return orderItem;
                    })
                    .collect(Collectors.toList());

            order.setOrderItems(orderItems);

            Order savedOrder = orderRepository.save(order);

            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("Error placing order: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Order>> getRecentOrders() {
        try {
            List<Order> recentOrders = orderRepository.findTop10ByOrderByOrderDateDesc();
            return new ResponseEntity<>(recentOrders, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching recent orders: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        try {
            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isPresent()) {
                return new ResponseEntity<>(order.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.err.println("Error fetching order by ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/sales/daily")
    public ResponseEntity<Map<String, Object>> getDailySales() {
        try {
            ZoneId zoneId = ZoneId.of("Asia/Colombo"); // Use your timezone
            LocalDateTime startOfDay = LocalDateTime.now(zoneId).with(LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.now(zoneId).with(LocalTime.MAX);

            Double totalDailySales = orderRepository.sumTotalAmountByStatusAndDateRange(
                    "DELIVERED", // Or whatever status indicates a completed sale
                    startOfDay,
                    endOfDay
            );

            if (totalDailySales == null) {
                totalDailySales = 0.0; // Handle the case where there are no sales
            }

            Map<String, Object> response = Map.of(
                    "totalSales", totalDailySales,
                    "reportDate", LocalDateTime.now(zoneId).toLocalDate()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching daily sales: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/sales/range")
    public ResponseEntity<List<Map<String, Object>>> getSalesForDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate endDate) {
        try {
            ZoneId zoneId = ZoneId.of("Asia/Colombo"); // Use your timezone
            LocalDateTime startOfDay = startDate.atStartOfDay(); // Get LocalDateTime at start of day
            LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);   // Get LocalDateTime at end of day

            List<Object[]> salesData = orderRepository.sumTotalAmountByStatusAndDateRangeGroupedByDate(
                    "DELIVERED", // Or your completed status
                    startOfDay,
                    endOfDay
            );
            List<Map<String, Object>> result = salesData.stream().map(data -> {
                Date sqlDate = (Date) data[0]; // Cast to java.sql.Date
                LocalDate localDate = sqlDate.toLocalDate(); // Convert to java.time.LocalDate
                Double total = (Double) data[1];
                Map<String, Object> map = new HashMap<>();
                map.put("date", localDate.toString()); // Use LocalDate for date string
                map.put("totalSales", total);
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.err.println("Error fetching sales for date range: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}