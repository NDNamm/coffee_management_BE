package com.example.demo.dto.DashboardDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DailyRevenueDTO {
    private LocalDate date;
    private BigDecimal revenue;

    public DailyRevenueDTO(java.sql.Date date, BigDecimal revenue) {
        this.date = date.toLocalDate();
        this.revenue = revenue;
    }
}
