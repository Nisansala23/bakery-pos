package com.example.bakery_pos.controller;

import com.example.bakery_pos.entity.Order;
import com.example.bakery_pos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();

        data.put("greeting", "Welcome to the Bakery POS System!");

        long pendingOrders = orderRepository.countByStatus("PENDING");
        data.put("pendingOrders", pendingOrders);

        // Calculate today's sales summary using Colombo time
        ZoneId zoneId = ZoneId.of("Asia/Colombo");
        LocalDateTime now = LocalDateTime.now(zoneId);
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        String deliveredStatus = "DELIVERED";

        Double totalSalesToday = orderRepository.sumTotalAmountByStatusAndDateRange(deliveredStatus, startOfDay, endOfDay);
        Long transactionsToday = orderRepository.countByStatusAndDateRange(deliveredStatus, startOfDay, endOfDay);

        data.put("totalSalesToday", totalSalesToday != null ? totalSalesToday : 0.0);
        data.put("salesTransactionsToday", transactionsToday != null ? transactionsToday : 0);

        // Recent delivered orders
        List<Order> recentDeliveredOrders = orderRepository.findTop5ByStatusOrderByOrderDateDesc(deliveredStatus);
        data.put("recentDeliveredOrders", recentDeliveredOrders != null ? recentDeliveredOrders : List.of());

        return data;
    }
}
