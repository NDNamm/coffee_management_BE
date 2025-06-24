package com.example.coffee.management.dto.DashboardDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MonthlyRevenueDTO {
    private int month;
    private BigDecimal revenue;
    private Long orderCount;

    public MonthlyRevenueDTO(int month, BigDecimal revenue, Long orderCount) {
        this.month = month;
        this.revenue = revenue;
        this.orderCount = orderCount;
    }
}
