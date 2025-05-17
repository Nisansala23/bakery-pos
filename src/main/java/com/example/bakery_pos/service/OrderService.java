package com.example.bakery_pos.service;

import com.example.bakery_pos.dto.OrderRequest;
import com.example.bakery_pos.entity.Order;
import com.example.bakery_pos.entity.OrderItem;
import com.example.bakery_pos.entity.Product;
import com.example.bakery_pos.repository.OrderItemRepository;
import com.example.bakery_pos.repository.OrderRepository;
import com.example.bakery_pos.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now(ZoneId.of("Asia/Colombo")));
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId()));

                    if (product.getStockQuantity() < itemDto.getQuantity()) {
                        throw new RuntimeException("Insufficient stock for product: " + product.getName());
                    }

                    product.setStockQuantity(product.getStockQuantity() - itemDto.getQuantity());
                    productRepository.save(product);

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
        orderItems.forEach(orderItem -> orderItemRepository.save(orderItem));

        return savedOrder;
    }

    public List<Order> getRecentOrders() {
        return orderRepository.findTop10ByOrderByOrderDateDesc();
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus("PENDING");
    }

    public Map<String, Object> getDailySales() {
        ZoneId zoneId = ZoneId.of("Asia/Colombo");
        LocalDateTime startOfDay = LocalDateTime.now(zoneId).with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now(zoneId).with(LocalTime.MAX);

        Double totalDailySales = orderRepository.sumTotalAmountByStatusAndDateRange(
                "DELIVERED",
                startOfDay,
                endOfDay
        );

        if (totalDailySales == null) {
            totalDailySales = 0.0;
        }

        return Map.of(
                "totalSales", totalDailySales,
                "reportDate", LocalDateTime.now(zoneId).toLocalDate()
        );
    }

    public List<Map<String, Object>> getSalesForDateRange(LocalDate startDate, LocalDate endDate) {
        ZoneId zoneId = ZoneId.of("Asia/Colombo");
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

        List<Object[]> salesData = orderRepository.sumTotalAmountByStatusAndDateRangeGroupedByDate(
                startOfDay,
                endOfDay
        );

        return salesData.stream().map(data -> {
            LocalDate localDate = ((java.sql.Date) data[0]).toLocalDate();
            Double total = (Double) data[1];
            Map<String, Object> map = new HashMap<>();
            map.put("date", localDate.toString());
            map.put("totalSales", total);
            return map;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getTodaySalesTrend() {
        ZoneId zoneId = ZoneId.of("Asia/Colombo");
        LocalDateTime startOfDay = LocalDate.now(zoneId).atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now(zoneId).atTime(LocalTime.MAX);
        List<Order> todayOrders = orderRepository.findByStatusAndOrderDateBetween("DELIVERED", startOfDay, endOfDay);

        int intervalMinutes = 30;
        LocalDateTime currentTime = startOfDay;
        List<Map<String, Object>> salesTrendData = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        while (currentTime.isBefore(endOfDay)) {
            LocalDateTime nextTime = currentTime.plusMinutes(intervalMinutes);
            double intervalSales = 0;
            for (Order order : todayOrders) {
                if (!order.getOrderDate().isBefore(currentTime) && order.getOrderDate().isBefore(nextTime)) {
                    intervalSales += order.getTotalAmount();
                }
            }
            salesTrendData.add(Map.of("time", currentTime.format(timeFormatter), "sales", intervalSales));
            currentTime = nextTime;
        }

        return salesTrendData;
    }
}