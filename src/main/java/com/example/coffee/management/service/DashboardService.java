package com.example.coffee.management.service;

import com.example.coffee.management.dto.DashboardDTO.DailyRevenueDTO;
import com.example.coffee.management.dto.DashboardDTO.MonthlyRevenueDTO;
import com.example.coffee.management.dto.DashboardDTO.TopProductDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface DashboardService {
    List<DailyRevenueDTO> getDailyRevenue(LocalDateTime from, LocalDateTime to);
    List<MonthlyRevenueDTO> getMonthlyRevenue(int year);
    List<Object[]> getOrderStatusCount();
    List<TopProductDTO> getTopProduct();
}
