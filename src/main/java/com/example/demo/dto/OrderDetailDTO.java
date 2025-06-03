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
    private String productName;
    private BigDecimal totalPrice;
    private ProductDTO productDTO;
    private String urlProductImage;

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
