package com.example.bakery_pos.dto;

import java.util.List;

public class OrderRequest {
    private List<OrderItemDto> orderItems;
    private double totalAmount;

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
