package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDetailDTO {
    private Long id;
    private Long quantity;
    private BigDecimal price;
    private LocalDateTime orderDate;
    private String productName;
}
