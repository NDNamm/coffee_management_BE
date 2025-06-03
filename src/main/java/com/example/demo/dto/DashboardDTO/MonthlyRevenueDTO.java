package com.example.demo.dto.DashboardDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MonthlyRevenueDTO {
    private int month;
    private BigDecimal revenue;
    public MonthlyRevenueDTO(int month, BigDecimal revenue) {
        this.month = month;
        this.revenue = revenue;
    }
}
