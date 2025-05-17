package com.example.bakery_pos.repository;

import com.example.bakery_pos.entity.Order;
<<<<<<< HEAD
=======
import org.springframework.data.domain.Pageable;
>>>>>>> f3685ca3c64026ae8f7165bcffdf7a540b04967c
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    long countByStatus(String status);

    @Query("SELECT SUM(o.totalAmount) FROM order o WHERE o.status = :status AND o.orderDate >= :startDate AND o.orderDate < :endDate")
    Double sumTotalAmountByStatusAndDateRange(
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(o) FROM order o WHERE o.status = :status AND o.orderDate >= :startDate AND o.orderDate < :endDate")
    Long countByStatusAndDateRange(
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Order> findTop5ByStatusOrderByOrderDateDesc(String status);

    List<Order> findTop10ByOrderByOrderDateDesc();
<<<<<<< HEAD

    @Query("SELECT DATE(o.orderDate), SUM(o.totalAmount) FROM order o WHERE o.status = 'DELIVERED' AND o.orderDate BETWEEN :startDate AND :endDate GROUP BY DATE(o.orderDate) ORDER BY DATE(o.orderDate)")
    List<Object[]> sumTotalAmountByStatusAndDateRangeGroupedByDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Order> findByStatus(String status);

    List<Order> findByOrderDateBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Order> findByStatusAndOrderDateBetween(String status, LocalDateTime startTime, LocalDateTime endTime);
=======
    @Query("SELECT DATE(o.orderDate), SUM(o.totalAmount) FROM order o WHERE o.status = 'DELIVERED' AND o.orderDate BETWEEN :startDate AND :endDate GROUP BY DATE(o.orderDate) ORDER BY DATE(o.orderDate)")
    List<Object[]> sumTotalAmountByStatusAndDateRangeGroupedByDate(
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
>>>>>>> f3685ca3c64026ae8f7165bcffdf7a540b04967c
}