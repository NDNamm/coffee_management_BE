package com.example.coffee.management.dto.DashboardDTO;

import lombok.Getter;
import lombok.Setter;

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
