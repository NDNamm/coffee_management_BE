package com.example.coffee.management.dto;

import com.example.coffee.management.model.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PaymentDTO {
    private Long id;
    private LocalDate paymentDate;
    private String paymentName;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
}
