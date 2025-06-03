package com.example.demo.service.ServiceImpl;

import com.example.demo.dto.DashboardDTO.DailyRevenueDTO;
import com.example.demo.dto.DashboardDTO.MonthlyRevenueDTO;
import com.example.demo.dto.DashboardDTO.OrderStatusCountDTO;
import com.example.demo.dto.DashboardDTO.TopProductDTO;
import com.example.demo.repository.DashboardRepository;
import com.example.demo.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
