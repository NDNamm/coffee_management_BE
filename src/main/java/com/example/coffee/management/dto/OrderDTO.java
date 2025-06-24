package com.example.coffee.management.dto;

import com.example.coffee.management.model.Address;
import com.example.coffee.management.model.enums.OrderStatus;
import com.example.coffee.management.model.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "NAME_ORDER_INVALID")
    @Size(min = 2, message = "NAME_ORDER_INVALID")
    private String name;

    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private String note;
    private PaymentMethod paymentMethod;
    private List<OrderDetailDTO> orderDetailDTO;
    private List<ItemDTO> items;
    private AddressDTO addressDTO;

}
