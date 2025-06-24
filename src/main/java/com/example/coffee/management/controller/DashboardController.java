package com.example.coffee.management.controller;

import com.example.coffee.management.dto.DashboardDTO.DailyRevenueDTO;
import com.example.coffee.management.dto.DashboardDTO.MonthlyRevenueDTO;
import com.example.coffee.management.dto.DashboardDTO.TopProductDTO;
import com.example.coffee.management.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/dashboard")
@RestController
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/revenue/daily")
    public ResponseEntity<List<DailyRevenueDTO>> getDailyRevenue(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(dashboardService.getDailyRevenue(from,to));
    }

    @GetMapping("/revenue/month")
    public ResponseEntity<List<MonthlyRevenueDTO>> getMonthlyRevenue(@RequestParam("year") int year) {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenue(year));
    }

    @GetMapping("/orders/status-count")
    public ResponseEntity<List<Object[]>> getOrderStatusCount() {
        return ResponseEntity.ok(dashboardService.getOrderStatusCount());
    }

    @GetMapping("/product/top-selling")
    public ResponseEntity<List<TopProductDTO>> getTopProduct() {
        return ResponseEntity.ok(dashboardService.getTopProduct());
    }

}
