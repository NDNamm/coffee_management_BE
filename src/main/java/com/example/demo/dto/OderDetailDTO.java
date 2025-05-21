package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OderDetailDTO {
    private Long id;
    private Long quantity;
    private BigDecimal price;
    private LocalDateTime orderDate;
}
