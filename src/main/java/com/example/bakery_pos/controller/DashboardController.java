package com.example.bakery_pos.controller;

import com.example.bakery_pos.entity.Order;
import com.example.bakery_pos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin; // Import this
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
public class DashboardController {

    @Autowired
    private OrderRepository orderRepository;
    @CrossOrigin(origins = "http://localhost:3000")

    @GetMapping("/api/dashboard")
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        data.put("greeting", "Welcome to the Bakery POS System!");
        data.put("pendingOrders", orderRepository.countByStatus("PENDING"));

// Calculate today's sales summary
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Colombo")); // Use Sri Lanka timezone
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().plusDays(1).atStartOfDay();
        String salesStatus = "DELIVERED";

        Double totalSalesToday = orderRepository.sumTotalAmountByStatusAndDateRange(salesStatus, startOfDay, endOfDay);
        Long salesTransactionsToday = orderRepository.countByStatusAndDateRange(salesStatus, startOfDay, endOfDay);

        data.put("totalSalesToday", totalSalesToday != null ? totalSalesToday : 0.0);
        data.put("salesTransactionsToday", salesTransactionsToday != null ? salesTransactionsToday : 0);

// Get recent delivered orders
        List<Order> recentDeliveredOrders = orderRepository.findTop5ByStatusOrderByOrderDateDesc("DELIVERED");
        data.put("recentDeliveredOrders", recentDeliveredOrders);




        return data;
    }
}
