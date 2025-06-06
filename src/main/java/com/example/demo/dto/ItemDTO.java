package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDTO {
    private Long productId;
    private Long quantity;
    private BigDecimal price;
}

