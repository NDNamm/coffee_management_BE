package com.example.coffee.management.service.impl;

import com.example.coffee.management.dto.DashboardDTO.DailyRevenueDTO;
import com.example.coffee.management.dto.DashboardDTO.MonthlyRevenueDTO;
import com.example.coffee.management.dto.DashboardDTO.TopProductDTO;
import com.example.coffee.management.repository.DashboardRepository;
import com.example.coffee.management.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    @Override
    public List<DailyRevenueDTO> getDailyRevenue(LocalDateTime from, LocalDateTime to) {
        return dashboardRepository.getRevenueByDay(from, to);
    }

    @Override
    public List<MonthlyRevenueDTO> getMonthlyRevenue(int year) {
        return dashboardRepository.getRevenueByMonth(year);
    }

    @Override
    public List<Object[]> getOrderStatusCount() {
        return dashboardRepository.getRevenueByStatus();
    }

    @Override
    public List<TopProductDTO> getTopProduct() {
        return dashboardRepository.getTopProduct();
    }
}
