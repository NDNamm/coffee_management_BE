package com.example.coffee.management.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long id;
    private Long quantity;
    private BigDecimal price;
    private String productName;
    private BigDecimal totalPrice;
    private String urlProductImage;

}
