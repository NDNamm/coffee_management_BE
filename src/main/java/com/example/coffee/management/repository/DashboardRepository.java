package com.example.coffee.management.repository;

import com.example.coffee.management.dto.DashboardDTO.DailyRevenueDTO;
import com.example.coffee.management.dto.DashboardDTO.MonthlyRevenueDTO;
import com.example.coffee.management.dto.DashboardDTO.TopProductDTO;
import com.example.coffee.management.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT new com.example.coffee.management.dto.DashboardDTO.DailyRevenueDTO(FUNCTION('date', o.orderDate), SUM(o.totalAmount), COUNT(o.id)) \
            FROM Order o \
            WHERE o.orderDate BETWEEN :start AND :end \
            AND o.status <> 'CANCELED'
            GROUP BY FUNCTION('date', o.orderDate) \
            ORDER BY FUNCTION('date', o.orderDate)""")
    List<DailyRevenueDTO> getRevenueByDay(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT new com.example.coffee.management.dto.DashboardDTO.MonthlyRevenueDTO(MONTH(o.orderDate), SUM(o.totalAmount),  COUNT(o.id)) \
            FROM Order o \
            WHERE YEAR(o.orderDate) = :year \
            AND o.status <> 'CANCELED'
            GROUP BY MONTH(o.orderDate) \
            ORDER BY MONTH(o.orderDate)""")
    List<MonthlyRevenueDTO> getRevenueByMonth(int year);

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> getRevenueByStatus();

    @Query("""
            SELECT new com.example.coffee.management.dto.DashboardDTO.TopProductDTO(od.product.namePro, SUM(od.quantity)) \
            FROM OrderDetail od \
            GROUP BY od.product.namePro \
            ORDER BY SUM(od.quantity) DESC""")
    List<TopProductDTO> getTopProduct();
}
