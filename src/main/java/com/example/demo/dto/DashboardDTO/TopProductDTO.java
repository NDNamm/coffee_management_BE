package com.example.demo.dto.DashboardDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TopProductDTO {
    private String productName;
    private Long totalSold;

    public TopProductDTO(String productName, Long totalSold) {
        this.productName = productName;
        this.totalSold = totalSold;
    }
}
