package com.example.coffee.management.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDTO {
    private Long productId;
    private Long quantity;
    private BigDecimal price;
}

