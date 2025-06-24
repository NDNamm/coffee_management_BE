package com.example.coffee.management.dto.DashboardDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusCountDTO {
    private String status;
    private Long count;
    public OrderStatusCountDTO(String status, Long count) {
        this.status = status;
        this.count = count;
    }
}
