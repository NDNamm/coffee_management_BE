package com.example.coffee.management.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Long id;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Long productId;
    private ProductDTO product;
}
