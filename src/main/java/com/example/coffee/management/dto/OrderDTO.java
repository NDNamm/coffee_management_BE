package com.example.coffee.management.dto;

import com.example.coffee.management.model.enums.OrderStatus;
import com.example.coffee.management.model.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String sessionId;
    private Long userId;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private String note;
    private PaymentMethod paymentMethod;
    private List<OrderDetailDTO> orderDetailDTO;
    private List<ItemDTO> items;
    private AddressDTO addressDTO;

}
