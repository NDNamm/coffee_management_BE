package com.example.demo.service;

import com.example.demo.dto.DashboardDTO.DailyRevenueDTO;
import com.example.demo.dto.DashboardDTO.MonthlyRevenueDTO;
import com.example.demo.dto.DashboardDTO.OrderStatusCountDTO;
import com.example.demo.dto.DashboardDTO.TopProductDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public interface DashboardService {
    List<DailyRevenueDTO> getDailyRevenue(LocalDateTime from, LocalDateTime to);
    List<MonthlyRevenueDTO> getMonthlyRevenue(int year);
    List<Object[]> getOrderStatusCount();
    List<TopProductDTO> getTopProduct();
}
