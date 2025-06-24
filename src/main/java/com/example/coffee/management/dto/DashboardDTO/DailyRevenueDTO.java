package com.example.coffee.management.dto.DashboardDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DailyRevenueDTO {
    private LocalDate date;
    private BigDecimal revenue;
    private Long orderCount;

    public DailyRevenueDTO(java.sql.Date date, BigDecimal revenue,Long orderCount) {
        this.date = date.toLocalDate();
        this.revenue = revenue;
        this.orderCount = orderCount;
    }
}
