package com.example.demo.repository;

import com.example.demo.dto.DashboardDTO.DailyRevenueDTO;
import com.example.demo.dto.DashboardDTO.MonthlyRevenueDTO;
import com.example.demo.dto.DashboardDTO.TopProductDTO;
import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Order, Long> {

    @Query("SELECT new com.example.demo.dto.DashboardDTO.DailyRevenueDTO(FUNCTION('date', o.orderDate), SUM(o.totalAmount)) " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN :start AND :end " +
            "GROUP BY FUNCTION('date', o.orderDate) " +
            "ORDER BY FUNCTION('date', o.orderDate)")
    List<DailyRevenueDTO> getRevenueByDay(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new com.example.demo.dto.DashboardDTO.MonthlyRevenueDTO(MONTH(o.orderDate), SUM(o.totalAmount)) " +
            "FROM Order o " +
            "WHERE YEAR(o.orderDate) = :year " +
            "GROUP BY MONTH(o.orderDate) " +
            "ORDER BY MONTH(o.orderDate)")
    List<MonthlyRevenueDTO> getRevenueByMonth(int year);

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> getRevenueByStatus();

    @Query("SELECT new com.example.demo.dto.DashboardDTO.TopProductDTO(od.product.name, SUM(od.quantity)) " +
            "FROM OrderDetail od " +
            "GROUP BY od.product.name " +
            "ORDER BY SUM(od.quantity) DESC")
    List<TopProductDTO> getTopProduct();
}
